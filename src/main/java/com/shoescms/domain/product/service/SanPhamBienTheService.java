package com.shoescms.domain.product.service;

import com.shoescms.domain.product.dto.BienTheGiaTriDTO;
import com.shoescms.domain.product.dto.SanPhamBienTheDTO;
import com.shoescms.domain.product.entitis.SanPhamBienTheEntity;
import com.shoescms.domain.product.models.SanPhamBienTheModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SanPhamBienTheService {

    Page<SanPhamBienTheDTO> filterEntities(Pageable pageable, Specification<SanPhamBienTheEntity> specification);
    SanPhamBienTheDTO add(SanPhamBienTheModel sanPhamBienTheModel);

    boolean deleteById(Long id);

    List<SanPhamBienTheDTO> saveAllStep2(List<SanPhamBienTheModel> models);

    List<BienTheGiaTriDTO> getListBienTheGiaTriByBienTheId(Long bienTheId);

    List<SanPhamBienTheDTO> findAllPhanLoaiTheoSanPham(Long id);

    void capNhatSoLuongSanPhamChoBienThe(Long id, int soLuong);
}
