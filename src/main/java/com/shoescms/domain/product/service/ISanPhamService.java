package com.shoescms.domain.product.service;

import com.shoescms.domain.product.dto.SanPhamBienTheDTO;
import com.shoescms.domain.product.dto.WebChiTietSanPhamDto;
import com.shoescms.domain.product.dto.SanPhamDto;
import com.shoescms.domain.product.dto.SanPhamFilterReqDto;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import com.shoescms.domain.product.enums.ELoaiBienThe;
import com.shoescms.domain.product.models.SanPhamModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ISanPhamService {
    Page<SanPhamDto> filterEntities(Pageable pageable, Specification<SanPhamEntity> specification);

    Page<SanPhamEntity> getAll(Pageable pageable);


    SanPhamDto themSuaSp(SanPhamModel model);


    boolean deleteById(Long id);

    void thayDoiPhanLoai(Long id, ELoaiBienThe type);

    ELoaiBienThe getPhanLoai(Long id);

    SanPhamDto findBydId(Long id);

    WebChiTietSanPhamDto chiTietSanPhamResDto(Long id);

    Page<WebChiTietSanPhamDto> locSPChoWeb(SanPhamFilterReqDto reqDto, Pageable pageable);

    List<SanPhamBienTheDTO> kiemTraSoLuongSpBienThe(List<Long> ids);

    void setMacDinhDanhMuc(Long id);

    void setMacDinhThuongHieu(Long id);
}
