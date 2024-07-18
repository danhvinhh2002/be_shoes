package com.shoescms.domain.product.service;

import com.shoescms.domain.product.entitis.ThuongHieuEntity;
import com.shoescms.domain.product.models.ThuongHieuModel;
import com.shoescms.domain.product.dto.ThuongHieuDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IThuongHieuService{

    Page<ThuongHieuEntity> filterEntities(Pageable pageable, Specification<ThuongHieuEntity> specification);

    ThuongHieuDTO add(ThuongHieuModel thuongHieuModel);

    ThuongHieuDTO update(ThuongHieuModel thuongHieuModel);

    boolean deleteById(Long id);

    List<ThuongHieuDTO> getThuongHieus(String tenThuongHieu, String slug,Pageable pageable);

    Page<ThuongHieuDTO> locThuongHieu(String tenThuongHieu, String layMacDinh, Pageable pageable);
}
