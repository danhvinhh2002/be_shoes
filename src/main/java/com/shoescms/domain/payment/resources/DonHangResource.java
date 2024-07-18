package com.shoescms.domain.payment.resources;

import com.shoescms.common.security.JwtTokenProvider;
import com.shoescms.common.service.MailService;
import com.shoescms.domain.payment.dtos.DonHangDto;
import com.shoescms.domain.payment.dtos.ETrangThaiDonHang;
import com.shoescms.domain.payment.dtos.LocDonHangReqDto;
import com.shoescms.domain.payment.dtos.ThemMoiDonHangReqDto;
import com.shoescms.domain.payment.entities.DiaChiEntity_;
import com.shoescms.domain.payment.entities.DonHangEntity;
import com.shoescms.domain.payment.entities.DonHangEntity_;
import com.shoescms.domain.payment.services.PaymentService;
import com.shoescms.domain.payment.services.DonHangServiceImpl;
import com.shoescms.domain.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "06. Don hang")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/don-hang")
public class DonHangResource {

    private final DonHangServiceImpl donHangService;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final MailService mailService;

    private final PaymentService paymentService;

    @GetMapping("/chi-tiet/{id}")
    public DonHangDto chiTietDonHang(@PathVariable Long id) {
        return donHangService.chiTietDonHang(id);
    }

    @PostMapping("/filter")
    public Page<DonHangDto> search(@RequestBody LocDonHangReqDto model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        List<Specification> specs = new ArrayList<>();
        if (model.getTrangThai() != null)
            specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DonHangEntity_.TRANG_THAI), model.getTrangThai())));
        if (!ObjectUtils.isEmpty(model.getMaDonHang()))
            specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DonHangEntity_.MA_DON_HANG), "%" + model.getMaDonHang() + "%")));
        if (!ObjectUtils.isEmpty(model.getTenNguoiNhan()))
            specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.join(DonHangEntity_.DIA_CHI_ENTITY).get(DiaChiEntity_.TEN_NGUOI_NHAN), "%" + model.getTenNguoiNhan() + "%")));
        if (!ObjectUtils.isEmpty(model.getPhuongThucTT()))
            specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DonHangEntity_.PHUONG_THUC_TT), model.getPhuongThucTT())));
        if (!ObjectUtils.isEmpty(model.getStartOrderDate()) && !ObjectUtils.isEmpty(model.getEndOrderDate()))
            specs.add(((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(DonHangEntity_.NGAY_TAO), model.getStartOrderDate(), model.getEndOrderDate())));

        Specification finalSpec = null;
        for (Specification spec : specs) {
            if (finalSpec == null)
                finalSpec = Specification.where(spec);
            else finalSpec.and(spec);
        }
        return donHangService.filterEntities(pageable, finalSpec);
    }

    @PatchMapping("cap-nhat-trang-thai/{id}")
    public void capNhatTrangThai(@PathVariable Long id, @RequestParam ETrangThaiDonHang trangThai, @RequestHeader(name = "x-api-token", required = false) String xApiToken) {
        donHangService.capNhatTrangThai(id, trangThai, Long.parseLong(jwtTokenProvider.getUserPk(xApiToken)));
    }

    // for admin or employee
    @PostMapping
    public void themMoiDonHang(@RequestBody ThemMoiDonHangReqDto reqDto, @RequestHeader(name = "x-api-token") String xApiToken) {
        if (xApiToken != null) // luu thong tin nguoi tao
            reqDto.setNguoiTao(Long.parseLong(jwtTokenProvider.getUserPk(xApiToken)));
        donHangService.themMoiDonHang(reqDto);
    }

    @PutMapping("cap-nhat/{id}")
    public void capNhatMoiDonHang(
            @PathVariable Long id,
            @RequestBody ThemMoiDonHangReqDto reqDto,
            @RequestHeader(name = "x-api-token") String xApiToken) {
        if (xApiToken != null) // luu thong tin nguoi tao
            reqDto.setNguoiTao(Long.parseLong(jwtTokenProvider.getUserPk(xApiToken)));
        reqDto.setOrderId(id);
        donHangService.themMoiDonHang(reqDto);
    }

    @GetMapping("/lich-su-da-mua")
    public Page<DonHangEntity> lichSuDaMua(@RequestHeader(name = "x-api-token") String xApiToken, @RequestParam(value = "trangThai", required = false) ETrangThaiDonHang trangThai,
                                           @PageableDefault(sort = "id", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(xApiToken));
        if (trangThai == null)
            return donHangService.findByNguoiMuaId(userId, pageable);
        return donHangService.findByNguoiMuaId(userId, trangThai, pageable);
    }

    @GetMapping("/public/tra-cuu")
    public List<DonHangDto> traCuuDonHang(@RequestParam String q) {
        return donHangService.traCuuDonHang(q);
    }
//    @PostMapping("/send-email")
//    public void sendEmail(@RequestParam("email") String email) {
//        try {
//            ClassPathResource resource = new ClassPathResource("html/mail-body.html");
//            InputStream inputStream = resource.getInputStream();
//            String mailBody = new BufferedReader(new InputStreamReader(inputStream))
//                    .lines().collect(Collectors.joining("\n"));
//            mailService.sendMail(email, "Đơn hàng thành công", mailBody);
//        } catch (IOException e) {
//            // Xử lý ngoại lệ nếu có lỗi khi đọc tệp
//            e.printStackTrace();
//            // Hoặc có thể ném một ngoại lệ khác hoặc thông báo lỗi tùy thuộc vào yêu cầu của bạn.
//        }
//    }

}
