package com.shoescms.domain.product.controller;

import com.shoescms.common.security.JwtTokenProvider;
import com.shoescms.domain.product.dto.*;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import com.shoescms.domain.product.enums.ELoaiBienThe;
import com.shoescms.domain.product.models.SanPhamBienTheModel;
import com.shoescms.domain.product.service.ISanPhamService;
import com.shoescms.domain.product.models.SanPhamModel;
import com.shoescms.domain.product.repository.ISanPhamRepository;
import com.shoescms.domain.product.service.impl.ISanPhamBienTheServiceImpl;
import com.shoescms.domain.product.entitis.SanPhamEntity_;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "03.1. San pham giay")
@RestController
@RequestMapping("/v1/san-pham")
public class SanPhamController {
    @Autowired
    private ISanPhamService iSanPhamService;
    @Autowired
    ISanPhamRepository iSanPhamRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ISanPhamBienTheServiceImpl iSanPhamBienTheService;

    @PostMapping(value = "/save-step1")
    public SanPhamDto themSuaSp(@RequestBody SanPhamModel model,
                                 @RequestHeader("x-api-token") String xApiToken) {
        model.setNguoiCapNhat(Long.parseLong(jwtTokenProvider.getUserPk(xApiToken)));
        if (model.getId() == null) model.setNguoiTao(model.getNguoiCapNhat());
        return this.iSanPhamService.themSuaSp(model);
    }

    @PatchMapping("/thay-doi-phan-loai/{id}")
    public void thayDoiPhanLoai(@PathVariable Long id, @RequestParam ELoaiBienThe type) {
        iSanPhamService.thayDoiPhanLoai(id, type);
    }

    @PostMapping("/save-step2")
    public SanPhamBienTheDTO save(@RequestBody SanPhamBienTheModel model) {
        return iSanPhamBienTheService.add(model);
    }

    @GetMapping("/phan-loai/{id}")
    public ELoaiBienThe getPhanLoai(@PathVariable Long id) {
        return iSanPhamService.getPhanLoai(id);
    }

    @DeleteMapping("/phan-loai/{id}")
    public ResponseDto delete(@PathVariable Long id) {
        return ResponseDto.of(iSanPhamBienTheService.deleteById(id));
    }

    @PatchMapping("/phan-loai/{id}/so-luong")
    public void capNhatSoLuongSanPhamChoBienThe(@PathVariable Long id, @RequestParam int soLuong) {
        iSanPhamBienTheService.capNhatSoLuongSanPhamChoBienThe(id, soLuong);
    }

    @GetMapping("{id}/phan-loai")
    public List<SanPhamBienTheDTO> findAllPhanLoaiTheoSanPham(@PathVariable Long id) {
        return iSanPhamBienTheService.findAllPhanLoaiTheoSanPham(id);
    }

    @GetMapping("/public/get-all-gia-tri-bien-the/{id}")
    public List<BienTheGiaTriDTO> getAllBienTheGiaTri(@Parameter(required = true, description = "bien the id", example = "1") @PathVariable Long id) {
        return iSanPhamBienTheService.getListBienTheGiaTriByBienTheId(id);
    }


    // Lay chi tiet San Pham theo id
    @GetMapping("{id}")
    public SanPhamDto chiTietSanPham(@PathVariable Long id) {
        return iSanPhamService.findBydId(id);
    }

    @PostMapping("/filter")
    public Page<SanPhamDto> search(@RequestBody SanPhamFilterReqDto model, Pageable pageable) {
        List<Specification<SanPhamEntity>> listSpect = new ArrayList<>();

        if (model.getQ() != null)
            listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(root.get(SanPhamEntity_.TIEU_DE), "%" + model.getQ().trim() + "%"),
                    criteriaBuilder.like(root.get(SanPhamEntity_.MA_SP), "%" + model.getQ().trim() + "%")
            ));

        if (model.getTieuDe() != null)
            listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(SanPhamEntity_.TIEU_DE), "%" + model.getTieuDe().trim() + "%"));
        if (model.getMaSp() != null)
            listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(SanPhamEntity_.MA_SP), "%" + model.getMaSp().trim() + "%"));
        if (model.getThuongHieu() != null)
            listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(SanPhamEntity_.THUONG_HIEU), model.getThuongHieu()));
        if (model.getDmGiay() != null)
            listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(SanPhamEntity_.DM_GIAY), model.getDmGiay()));
        if (model.getHienThiWeb() != null)
            listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(SanPhamEntity_.HIEN_THI_WEB), model.getHienThiWeb()));
        if (model.getCreatedAtRange() != null)
            listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(SanPhamEntity_.NGAY_TAO), model.getCreatedAtRange().get(0), model.getCreatedAtRange().get(1)));
        if (model.getTinhTrangKho() != null && model.getTinhTrangKho().equals("EMPTY"))
            listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(SanPhamEntity_.TONG_SP), 0));
        if (model.getTinhTrangKho() != null && model.getTinhTrangKho().equals("AVAIL"))
            listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(SanPhamEntity_.TONG_SP), 0));

        listSpect.add((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(SanPhamEntity_.NGAY_XOA)));
        Specification<SanPhamEntity> finalSpec = null;
        for (Specification spec : listSpect) {
            if (finalSpec == null) {
                finalSpec = Specification.where(spec);
            } else {
                finalSpec = finalSpec.and(spec);
            }
        }
        return iSanPhamService.filterEntities(pageable, finalSpec);
    }


//        @PutMapping("/update-step1/{id}")
//    public  ResponseDto updateSanPham(@RequestBody SanPhamModel model){
//           return ResponseDto.of(iSanPhamService.update(model));
//        }
    @DeleteMapping("/delete/{id}")
    public ResponseDto deleteSanPham(@PathVariable Long id) {
        return ResponseDto.of(iSanPhamService.deleteById(id));
    }


    // public API for user web
    @Operation(summary = "lấy chi tiết sp(user web)")
    @GetMapping("/public/{id}")
    public WebChiTietSanPhamDto chiTietSanPhamResDto(@PathVariable Long id) {
        return iSanPhamService.chiTietSanPhamResDto(id);
    }

    @Operation(summary = "Lọc sản phẩm(user web)")
    @PostMapping("/public")
    public Page<WebChiTietSanPhamDto> locSPChoWeb(@RequestBody SanPhamFilterReqDto reqDto, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        return iSanPhamService.locSPChoWeb(reqDto, pageable);
    }

    @GetMapping("/public/kiem-tra-soluong-sp-bien-the")
    public List<SanPhamBienTheDTO> kiemTraSoLuongSpBienThe(@RequestParam List<Long> ids) {
        return iSanPhamService.kiemTraSoLuongSpBienThe(ids);
    }
}
