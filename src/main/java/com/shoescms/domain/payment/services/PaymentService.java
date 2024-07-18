package com.shoescms.domain.payment.services;

import com.shoescms.common.config.CommonConfig;
import com.shoescms.common.exception.ObjectNotFoundException;
import com.shoescms.common.exception.ProcessFailedException;
import com.shoescms.common.service.MailService;
import com.shoescms.domain.cart.entity.GioHangChiTiet;
import com.shoescms.domain.cart.repository.IGioHangChiTietRepository;
import com.shoescms.domain.cart.repository.IGioHangRepository;
import com.shoescms.domain.payment.dtos.*;
import com.shoescms.domain.payment.entities.ChiTietDonHangEntity;
import com.shoescms.domain.payment.entities.DiaChiEntity;
import com.shoescms.domain.payment.entities.DonHangEntity;
import com.shoescms.domain.payment.repositories.IChiTietDonHangRepository;
import com.shoescms.domain.payment.repositories.IDiaChiRepository;
import com.shoescms.domain.payment.repositories.IDonHangRepository;
import com.shoescms.domain.payment.resources.VnPayConfig;
import com.shoescms.domain.product.dto.SanPhamMetadataResDto;
import com.shoescms.domain.product.entitis.SanPhamBienTheEntity;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import com.shoescms.domain.product.repository.IBienTheGiaTriRepository;
import com.shoescms.domain.product.repository.ISanPhamBienTheRepository;
import com.shoescms.domain.product.service.impl.ISanPhamBienTheServiceImpl;
import com.shoescms.domain.user.UserService;
import com.shoescms.domain.user.dto.UsermetaDto;
import com.shoescms.domain.user.repository.INguoiDungRepository;
import com.shoescms.domain.voucher.VoucherService;
import com.shoescms.domain.voucher.entity.EGiamGiaTheo;
import com.shoescms.domain.voucher.entity.VoucherEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Component
//@Log4j
@RequiredArgsConstructor
public class PaymentService {

    private final IDonHangRepository donHangRepository;
    private final IDiaChiRepository diaChiRepository;
    private final IGioHangChiTietRepository gioHangChiTietRepository;
    private final ISanPhamBienTheRepository sanPhamBienTheRepository;
    private final IGioHangRepository gioHangRepository;
    private final IChiTietDonHangRepository chiTietDonHangRepository;
    private final ISanPhamBienTheServiceImpl sanPhamBienTheService;

    private final UserService userService;
    private final INguoiDungRepository userRepository;
    private final CommonConfig commonConfig;
    private final VoucherService voucherService;

    private final IBienTheGiaTriRepository bienTheGiaTriRepository;
    private final MailService mailService;

    @Transactional
    public DonHangDto datHang(DatHangReqDto reqDto) {

        // luu thong tin don hang
        DonHangEntity donHangEntity = new DonHangEntity();
        donHangEntity.setNguoiMuaId(reqDto.getNguoiTao());
        donHangEntity.setTongTienGiamGia(BigDecimal.ZERO);
        //ma don hang
        donHangEntity.setMaDonHang(getRandomNumber(10));

        // luu thong tin chi tiet don hang
        List<ChiTietDonHangEntity> chiTietDonHangEntities = taoChiTietDonHangTuGioHangTamThoi(reqDto.getGioHangTamThoiReqDto(), donHangEntity);

        donHangEntity.setPhuongThucTT(reqDto.getPhuongThucTT());
        if (reqDto.getPhuongThucTT().equals(EPhuongThucTT.VNPAY))
            donHangEntity.setNgayXoa(LocalDateTime.now());

        donHangEntity.setTrangThai(ETrangThaiDonHang.WAITING_CONFIRM);
        donHangEntity.setGhiChu(reqDto.getNote());
        donHangEntity.setPhiShip(reqDto.getShipFee());
        donHangEntity.setTongGiaCuoiCung(reqDto.getTotalPay());

        // luu dia chi dat hang
        DiaChiDto diaChiDto;
        if (reqDto.getDiaChiId() == null) {
            DiaChiEntity diaChi = new DiaChiEntity();
            diaChi.setDiaChi(reqDto.getDiaChiNhanHang());
            diaChi.setSdt(reqDto.getSoDienThoaiNhanHang());
            diaChi.setEmail(reqDto.getEmail());
            diaChi.setTenNguoiNhan(reqDto.getHoTenNguoiNhan());
            diaChiRepository.saveAndFlush(diaChi);
            donHangEntity.setDiaChiEntity(diaChi);

            diaChiDto = DiaChiDto.toDto(diaChi);
        } else {
            DiaChiEntity diaChi = diaChiRepository.findById(reqDto.getDiaChiId()).orElseThrow(() -> new ObjectNotFoundException(1));
            donHangEntity.setDiaChiEntity(diaChi);
            diaChiDto = DiaChiDto.toDto(diaChi);
        }

        // set giam gia
        if (reqDto.getMaGiamGiaId() != null) {
            VoucherEntity voucherEntity = voucherService.getById(reqDto.getMaGiamGiaId());
            donHangEntity.setMaGiamGiaId(voucherEntity.getId());
            donHangEntity.setTongTienGiamGia(BigDecimal.ZERO);
            if (voucherEntity.getGiamGiaTheo().equals(EGiamGiaTheo.DIRECTLY))
                donHangEntity.setTongTienGiamGia(BigDecimal.valueOf(voucherEntity.getGiaGiam()));
            else
                donHangEntity.setTongTienGiamGia(donHangEntity.getTongGiaTien().multiply(BigDecimal.valueOf(voucherEntity.getPhanTramGiam() / 100)));
            donHangEntity.setTongGiaCuoiCung(donHangEntity.getTongGiaTien().subtract(donHangEntity.getTongTienGiamGia()));
            voucherEntity.setSoLuotDaDung(Optional.ofNullable(voucherEntity.getSoLuotDaDung()).orElse(0) + 1);
            voucherService.updateEntity(voucherEntity);
        }
        donHangRepository.saveAndFlush(donHangEntity);
        chiTietDonHangEntities.forEach(i -> i.setDonHang(donHangEntity.getId()));
        chiTietDonHangRepository.saveAllAndFlush(chiTietDonHangEntities);

        // xoa gio hang sau khi dat thanh cong
        if (reqDto.getNguoiTao() != null)
            gioHangChiTietRepository.deleteItemFromCart(reqDto.getGioHangTamThoiReqDto().stream().map(GioHangTamThoiReqDto::getSanPhamBienThe).toList(), gioHangRepository.findByNguoiDungId(reqDto.getNguoiTao()).getId());

        DonHangDto donHangDto = new DonHangDto();
        donHangDto.setId(donHangEntity.getId());
        donHangDto.setMaDonHang(donHangEntity.getMaDonHang());
        donHangDto.setTongSp(donHangEntity.getTongSp());
        donHangDto.setPhuongThucTT(donHangEntity.getPhuongThucTT());
        donHangDto.setTrangThai(donHangEntity.getTrangThai());
        donHangDto.setNgayTao(donHangEntity.getNgayTao());
        donHangDto.setPhiShip(donHangEntity.getPhiShip());
        donHangDto.setTongGiaCuoiCung(donHangEntity.getTongGiaCuoiCung());

        if (donHangEntity.getNguoiMuaId() != null)
            donHangDto.setNguoiMua(UsermetaDto.toDto(userRepository.findById(donHangEntity.getNguoiMuaId()).orElse(null)));
        if (donHangEntity.getNguoiCapNhat() != null)
            donHangDto.setNguoiCapNhat(UsermetaDto.toDto(userRepository.findById(donHangEntity.getNguoiCapNhat()).orElse(null)));

        List<ChiTietDonHangDto> chiTietDonHangDtos = new ArrayList<>();
        for (int i = 0; i < chiTietDonHangEntities.size(); i++) {
            ChiTietDonHangDto chiTietDonHangDto = new ChiTietDonHangDto();
            chiTietDonHangDto.setId(chiTietDonHangEntities.get(i).getId());

            SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(chiTietDonHangEntities.get(i).getPhanLoaiSpId()).orElse(null);
            chiTietDonHangDto.setSanPham(SanPhamMetadataResDto.toDto(sanPhamBienTheEntity.getSanPham()));
            chiTietDonHangDto.setPhanLoaiSpId(chiTietDonHangEntities.get(i).getPhanLoaiSpId());
            chiTietDonHangDto.setSoLuong(chiTietDonHangEntities.get(i).getSoLuong());
            chiTietDonHangDto.setGiaTien(chiTietDonHangEntities.get(i).getGiaTien());
            chiTietDonHangDto.setMotaPhanLoai(sanPhamBienTheEntity.getMotaPhanLoai());

            chiTietDonHangDtos.add(chiTietDonHangDto);
        }

        donHangDto.setDiaChi(diaChiDto);
        donHangDto.setChiTietDonHang(chiTietDonHangDtos);


        donHangDto.getChiTietDonHang().forEach(item -> {
            SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(item.getPhanLoaiSpId()).orElseThrow(() -> new ProcessFailedException("failed"));
            item.setSanPham(SanPhamMetadataResDto.toDto(sanPhamBienTheEntity.getSanPham()));
        });

        if (reqDto.getEmail() != null)
            new Thread(() -> {
                try {
                    System.out.println("don Hang gui mail " + donHangDto);
                    Map<String, Object> context = new HashMap<>();
                    context.put("order", donHangDto);
                    System.out.println("send mail to " + reqDto.getEmail());
                    mailService.sendEmail("html/mail-order.html", reqDto.getEmail(), "Thông tin đặt hàng", context);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }).start();

        return donHangDto;
    }

    private List<ChiTietDonHangEntity> taoChiTietDonHangTuGioHangTamThoi(List<GioHangTamThoiReqDto> gioHangTamThoiReqDto, DonHangEntity donHangEntity) {
        List<ChiTietDonHangEntity> chiTietDonHangEntities = new ArrayList<>();
        BigDecimal tongTien = new BigDecimal(0);
        Integer tongSanPham = 0;

        for (int i = 0; i < gioHangTamThoiReqDto.size(); i++) {
            SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(gioHangTamThoiReqDto.get(i).getSanPhamBienThe()).orElseThrow(() -> new ObjectNotFoundException(8));
            SanPhamEntity sanPhamEntity = sanPhamBienTheEntity.getSanPham();
            BigDecimal tongTienSp = sanPhamEntity.getGiaMoi().multiply(BigDecimal.valueOf(gioHangTamThoiReqDto.get(i).getSoLuong().doubleValue()));
            tongTien = tongTien.add(tongTienSp);
            tongSanPham += gioHangTamThoiReqDto.get(i).getSoLuong();

            if (sanPhamBienTheEntity.getSoLuong() < gioHangTamThoiReqDto.get(i).getSoLuong())
                throw new ObjectNotFoundException(60);

            // tao thong tin
            ChiTietDonHangEntity chiTietDonHang = new ChiTietDonHangEntity();
            chiTietDonHang.setSoLuong(gioHangTamThoiReqDto.get(i).getSoLuong());
            chiTietDonHang.setGiaTien(sanPhamEntity.getGiaMoi());
            chiTietDonHang.setPhanLoaiSpId(sanPhamBienTheEntity.getId());
            chiTietDonHang.setMotaPhanLoai(sanPhamBienTheEntity.getMotaPhanLoai());
            chiTietDonHang.setSpId(sanPhamEntity.getId());
            chiTietDonHangEntities.add(chiTietDonHang);
        }

        donHangEntity.setTongSp(tongSanPham);
        donHangEntity.setTongTienSP(tongTien);
        donHangEntity.setTongGiaTien(tongTien);
        return chiTietDonHangEntities;
    }

    private void taoChiTietDonHangTuGioHangChiTiet(List<Long> gioHangItems, DonHangEntity donHangEntity) {
        List<GioHangChiTiet> gioHangChiTiets = gioHangChiTietRepository.findAllById(gioHangItems);
        List<ChiTietDonHangEntity> chiTietDonHangEntities = new ArrayList<>();
        BigDecimal tongTien = new BigDecimal(0);
        Integer tongSanPham = 0;

        for (int i = 0; i < gioHangChiTiets.size(); i++) {
            SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(gioHangChiTiets.get(i).getSanPhamBienThe()).orElseThrow(() -> new ObjectNotFoundException(8));
            SanPhamEntity sanPhamEntity = sanPhamBienTheEntity.getSanPham();
            BigDecimal tongTienSp = sanPhamEntity.getGiaMoi().multiply(BigDecimal.valueOf(gioHangChiTiets.get(i).getSoLuong().doubleValue()));
            tongTien.add(tongTienSp);
            tongSanPham += gioHangChiTiets.get(i).getSoLuong();
            // tao thong tin
            ChiTietDonHangEntity chiTietDonHang = new ChiTietDonHangEntity();
            chiTietDonHang.setSoLuong(gioHangChiTiets.get(i).getSoLuong());
            chiTietDonHang.setGiaTien(sanPhamEntity.getGiaMoi());
            chiTietDonHang.setPhanLoaiSpId(sanPhamBienTheEntity.getId());
            chiTietDonHangEntities.add(chiTietDonHang);
        }

        donHangEntity.setTongSp(tongSanPham);
        donHangEntity.setTongGiaTien(tongTien);
        donHangEntity.setChiTietDonHangs(chiTietDonHangEntities);
    }

    public String taoUrlVnpay(DonHangDto dto) throws UnsupportedEncodingException {
        // thanh toan vnpay
        String vnp_OrderInfo = "Thanh toan don hang";//Thông tin mô tả nội dung thanh toán;
        String vnp_TxnRef = dto.getMaDonHang(); //Mã tham chiếu của giao dịch tại hệ thống của merchant.
        String bank_code = ""; // edit later

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "ATM";
        String vnp_IpAddr = "0:0:0:0:0:0:0:1";
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;
        BigDecimal amount = dto.getTongGiaCuoiCung().multiply(BigDecimal.valueOf(100)); //gia tien don hang (gán tạm)

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount.toBigInteger()));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(dt);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        Calendar cldvnp_ExpireDate = Calendar.getInstance();
        cldvnp_ExpireDate.add(Calendar.MINUTE, 15);
        Date vnp_ExpireDateD = cldvnp_ExpireDate.getTime();

        System.out.println("expireDate: " + vnp_ExpireDateD);
        String vnp_ExpireDate = formatter.format(vnp_ExpireDateD);

        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                // hashData.append(fieldValue); //sử dụng và 2.0.0 và 2.0.1 checksum sha256
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())); // sử dụng v2.1.0
                // check sum
                // sha512
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        System.out.println("hash: " + vnp_SecureHash);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    public String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public Long getDonHang(Long id) {
        return null;
    }


    @Transactional
    public String xuLyThanhToanVnpay(VnpayRedirectReqDto reqDto) throws IOException {
        DonHangEntity donHangEntity = donHangRepository.findByMaDonHang(reqDto.getVnp_TxnRef());

        StringBuilder resParameter = new StringBuilder("redirect:")
                .append(commonConfig.getVnpayRedirectURl())
                .append("?status=");
        if (!reqDto.getVnp_TransactionStatus().equals("00") || donHangEntity == null) // failed order
            resParameter.append("FAILED");
        else { // success
            donHangEntity.setTrangThai(ETrangThaiDonHang.VNPAY_PAID);
            donHangEntity.setNgayXoa(null);
            // need save vnpay info
            donHangRepository.saveAndFlush(donHangEntity);
            resParameter.append("SUCCESS&id=").append(donHangEntity.getId());
        }
        return resParameter.toString();
    }
}
