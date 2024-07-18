package com.shoescms.domain.payment.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoescms.common.exception.ObjectNotFoundException;
import com.shoescms.common.exception.ProcessFailedException;
import com.shoescms.common.model.repositories.FileRepository;
import com.shoescms.domain.payment.dtos.*;
import com.shoescms.domain.payment.entities.ChiTietDonHangEntity;
import com.shoescms.domain.payment.entities.DiaChiEntity;
import com.shoescms.domain.payment.entities.DonHangEntity;
import com.shoescms.domain.payment.repositories.IChiTietDonHangRepository;
import com.shoescms.domain.payment.repositories.IDiaChiRepository;
import com.shoescms.domain.payment.repositories.IDonHangRepository;
import com.shoescms.domain.payment.services.PaymentService;
import com.shoescms.domain.product.dto.SanPhamMetadataResDto;
import com.shoescms.domain.product.entitis.SanPhamBienTheEntity;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import com.shoescms.domain.product.repository.IBienTheGiaTriRepository;
import com.shoescms.domain.product.repository.ISanPhamBienTheRepository;
import com.shoescms.domain.product.repository.ISanPhamRepository;
import com.shoescms.domain.product.service.impl.ISanPhamBienTheServiceImpl;
import com.shoescms.domain.user.dto.UsermetaDto;
import com.shoescms.domain.user.repository.INguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.shoescms.domain.payment.entities.QDonHangEntity.donHangEntity;

@Service
@RequiredArgsConstructor
public class DonHangServiceImpl  {
    private final ISanPhamBienTheRepository sanPhamBienTheRepository;
    private final IBienTheGiaTriRepository bienTheGiaTriRepository;
    private final FileRepository fileRepository;
    private final IDonHangRepository donHangRepository;
    private final IChiTietDonHangRepository chiTietDonHangRepository;
    private final ISanPhamRepository sanPhamRepository;
    private final IDiaChiRepository diaChiRepository;
    private final ISanPhamBienTheServiceImpl sanPhamBienTheService;
    private final INguoiDungRepository userRepository;

    private final JPAQueryFactory jpaQueryFactory;
    private final PaymentService paymentService;

    public Page<DonHangDto> filterEntities(Pageable pageable, Specification<DonHangEntity> specification) {
        Page<DonHangEntity> donHangDtoPage = donHangRepository.findAll(specification, pageable);
        return donHangDtoPage.map(donHangEntity -> {
            DonHangDto dto = DonHangDto.toDto(donHangEntity);
            if (donHangEntity.getNguoiMuaId() != null)
                dto.setNguoiMua(UsermetaDto.toDto(userRepository.findById(donHangEntity.getNguoiMuaId()).orElse(null)));
            if (donHangEntity.getNguoiCapNhat() != null)
                dto.setNguoiCapNhat(UsermetaDto.toDto(userRepository.findById(donHangEntity.getNguoiCapNhat()).orElse(null)));
            return dto;
        });
    }

    public DonHangDto chiTietDonHang(Long id) {
        DonHangEntity donHangEntity = donHangRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(51));
        DonHangDto dto = DonHangDto.toDto(donHangEntity);
        dto.getChiTietDonHang().forEach(item -> {
            SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(item.getPhanLoaiSpId()).orElseThrow(() -> new ProcessFailedException("failed"));
            item.setSanPham(SanPhamMetadataResDto.toDto(sanPhamBienTheEntity.getSanPham()));
            item.getSanPham().setAnhChinh(fileRepository.findById(sanPhamBienTheEntity.getSanPham().getAnhChinh()).orElse(null));
        });

        if (donHangEntity.getNguoiMuaId() != null)
            dto.setNguoiMua(UsermetaDto.toDto(userRepository.findById(donHangEntity.getNguoiMuaId()).orElse(null)));
        if (donHangEntity.getNguoiCapNhat() != null)
            dto.setNguoiCapNhat(UsermetaDto.toDto(userRepository.findById(donHangEntity.getNguoiCapNhat()).orElse(null)));
        return dto;
    }

    @Transactional
    public void capNhatTrangThai(Long id, ETrangThaiDonHang trangThai, Long userId) {
        DonHangEntity donHangEntity = donHangRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(51));
        donHangEntity.setTrangThai(trangThai);
        donHangEntity.setNguoiCapNhat(userId);

        if (trangThai.equals(ETrangThaiDonHang.DELIVERING))
            donHangEntity
                    .getChiTietDonHangs()
                    .forEach(gioHangChiTiet -> {
                        SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(gioHangChiTiet.getPhanLoaiSpId()).orElseThrow(() -> new ObjectNotFoundException(8));
                        int newSoLuong = sanPhamBienTheEntity.getSoLuong() - gioHangChiTiet.getSoLuong();
                        sanPhamBienTheService.capNhatSoLuongSanPhamChoBienThe(sanPhamBienTheEntity.getId(), Math.max(newSoLuong, 0));
                    });

        if (trangThai.equals(ETrangThaiDonHang.COMPLETED)) {
            donHangEntity.getChiTietDonHangs()
                    .forEach(sp -> {
                        SanPhamEntity sanPhamEntity = sanPhamRepository.findById(sp.getSpId()).orElse(null);
                        if (sanPhamEntity != null) {
                            int daBan = Optional.ofNullable(sanPhamEntity.getDaBan()).orElse(0);
                            sanPhamEntity.setDaBan(daBan + sp.getSoLuong());
                            sanPhamRepository.saveAndFlush(sanPhamEntity);
                        }
                    });
        }
        donHangRepository.saveAndFlush(donHangEntity);
    }

    @Transactional
    public DonHangDto themMoiDonHang(ThemMoiDonHangReqDto reqDto) {
        DonHangEntity donHangEntity;
        if (reqDto.getOrderId() != null) {
            donHangEntity = donHangRepository.findById(reqDto.getOrderId()).orElseThrow(() -> new ProcessFailedException("failed to find original order"));
            chiTietDonHangRepository.saveAllAndFlush(
                    donHangEntity
                            .getChiTietDonHangs()
                            .stream()
                            .peek(ct -> ct.setNgayXoa(LocalDateTime.now()))
                            .toList()
            );
//            chiTietDonHangRepository.deleteAllById(donHangEntity
//                    .getChiTietDonHangs().stream().map(ChiTietDonHangEntity::getId).toList());
            donHangEntity.setChiTietDonHangs(null);
        } else donHangEntity = new DonHangEntity();

        if (reqDto.getOrderId() == null)
            donHangEntity.setMaDonHang(paymentService.getRandomNumber(10));
        donHangEntity.setPhuongThucTT(reqDto.getPhuongThucTT());

        AtomicReference<BigDecimal> tongTien = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<Integer> tongSanPham = new AtomicReference<>(0);
        List<ChiTietDonHangEntity> chiTietDonHangEntities = reqDto.getPhanLoaidIds()
                .stream()
                .map(sp -> {
                    SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(sp.getSanPhamBienThe()).orElseThrow(() -> new ObjectNotFoundException(8));
                    tongTien.updateAndGet(v -> v.add(sanPhamBienTheEntity.getSanPham().getGiaMoi().multiply(BigDecimal.valueOf(sp.getSoLuong()))));
                    tongSanPham.updateAndGet(v -> v + sp.getSoLuong());
                    return ChiTietDonHangEntity
                            .builder()
                            .phanLoaiSpId(sp.getSanPhamBienThe())
                            .soLuong(sp.getSoLuong())
                            .giaTien(sanPhamBienTheEntity.getSanPham().getGiaMoi())
                            .spId(sanPhamBienTheEntity.getSanPham().getId())
                            .motaPhanLoai(sanPhamBienTheEntity.getMotaPhanLoai())
                            .build();
                })
                .toList();

        if (reqDto.getNguoiDat() != null)
            donHangEntity.setNguoiMuaId(reqDto.getNguoiDat());
        if (reqDto.getNguoiTao() != null)
            donHangEntity.setNguoiCapNhat(reqDto.getNguoiTao());

        donHangEntity.setGhiChu(reqDto.getGhiChu());
        donHangEntity.setTongSp(tongSanPham.get());
        donHangEntity.setTongGiaTien(tongTien.get());
        donHangEntity.setTrangThai(ETrangThaiDonHang.WAITING_CONFIRM);
        donHangEntity.setTongTienGiamGia(reqDto.getDiscount());
        donHangEntity.setPhiShip(reqDto.getShipFee());
        donHangEntity.setTongGiaCuoiCung(reqDto.getTotalPay());

        DiaChiEntity diaChi = new DiaChiEntity();
        donHangEntity.setDiaChiEntity(diaChi);
        diaChi.setDiaChi(reqDto.getDiaChiNhanHang());
        diaChi.setSdt(reqDto.getSoDienThoaiNhanHang());
        diaChi.setTenNguoiNhan(reqDto.getHoTenNguoiNhan());

        diaChiRepository.saveAndFlush(diaChi);
        donHangRepository.saveAndFlush(donHangEntity);
        chiTietDonHangEntities.forEach(i -> i.setDonHang(donHangEntity.getId()));
        chiTietDonHangRepository.saveAllAndFlush(chiTietDonHangEntities);


        DonHangDto donHangDto = new DonHangDto();
        donHangDto.setId(donHangEntity.getId());
        donHangDto.setMaDonHang(donHangEntity.getMaDonHang());
        donHangDto.setTongSp(donHangEntity.getTongSp());
        donHangDto.setPhuongThucTT(donHangEntity.getPhuongThucTT());
        donHangDto.setTrangThai(donHangEntity.getTrangThai());
        donHangDto.setNgayTao(donHangEntity.getNgayTao());

        donHangDto.setTongTienSp(donHangEntity.getTongTienSP());
        donHangDto.setTongTienGiamGia(donHangEntity.getTongTienGiamGia());
        donHangDto.setPhiShip(donHangEntity.getPhiShip());
        donHangDto.setTongGiaTien(donHangEntity.getTongGiaTien());
        donHangDto.setTongGiaCuoiCung(donHangEntity.getTongGiaTien());

        List<ChiTietDonHangDto> chiTietDonHangDtos = new ArrayList<>();
        for (ChiTietDonHangEntity chiTietDonHangEntity : chiTietDonHangEntities) {
            ChiTietDonHangDto chiTietDonHangDto = new ChiTietDonHangDto();
            chiTietDonHangDto.setId(chiTietDonHangEntity.getId());

            SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(chiTietDonHangEntity.getPhanLoaiSpId()).orElse(null);
            chiTietDonHangDto.setSanPham(SanPhamMetadataResDto.toDto(sanPhamBienTheEntity.getSanPham()));
            chiTietDonHangDto.setPhanLoaiSpId(chiTietDonHangEntity.getPhanLoaiSpId());
            chiTietDonHangDto.setSoLuong(chiTietDonHangEntity.getSoLuong());
            chiTietDonHangDto.setGiaTien(chiTietDonHangEntity.getGiaTien());
            chiTietDonHangDtos.add(chiTietDonHangDto);
        }

        DiaChiDto diaChiDto = new DiaChiDto();
        diaChiDto.setId(diaChi.getId());
        diaChiDto.setTenNguoiNhan(diaChi.getTenNguoiNhan());
        diaChiDto.setSdt(diaChi.getSdt());
        diaChiDto.setDiaChi(diaChi.getDiaChi());
        donHangDto.setDiaChi(diaChiDto);
        donHangDto.setChiTietDonHang(chiTietDonHangDtos);


        return donHangDto;
    }

    public Page<DonHangEntity> findByNguoiMuaId(Long nguoiMuaId,ETrangThaiDonHang trangThai, Pageable pageable) {
        return donHangRepository.findByNguoiMuaId(nguoiMuaId, trangThai, pageable);
    }

    public Page<DonHangEntity> findByNguoiMuaId(Long nguoiMuaId, Pageable pageable  ) {
        return donHangRepository.findByNguoiMuaId(nguoiMuaId, pageable);
    }

    public List<DonHangDto> traCuuDonHang(String q) {
        return jpaQueryFactory.selectFrom(donHangEntity)
                .where(
                        donHangEntity.ngayXoa.isNull(),
                        donHangEntity.maDonHang.eq(q).or(donHangEntity.diaChiEntity.sdt.eq(q))
                )
                .fetch()
                .stream()
                .map(dh -> {
                    DonHangDto dto = DonHangDto.toDto(dh);
                    dto.getChiTietDonHang().forEach(item -> {
                        SanPhamBienTheEntity sanPhamBienTheEntity = sanPhamBienTheRepository.findById(item.getPhanLoaiSpId()).orElseThrow(() -> new ProcessFailedException("failed"));
                        item.setSanPham(SanPhamMetadataResDto.toDto(sanPhamBienTheEntity.getSanPham()));
                        item.getSanPham().setAnhChinh(fileRepository.findById(sanPhamBienTheEntity.getSanPham().getAnhChinh()).orElse(null));
                    });
                    return dto;
                })
                .toList();
    }


}
