package com.shoescms.domain.product.service;

import com.shoescms.domain.product.entitis.DMGiayEntity;
import com.shoescms.domain.product.models.DanhMucGiayModel;
import com.shoescms.domain.product.dto.DanhMucDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IDanhMucGiayService {

    Page<DMGiayEntity> filterEntities(Pageable pageable, Specification<DMGiayEntity> specification);

    DanhMucDTO add(DanhMucGiayModel danhMucGiayModel);

    DanhMucDTO update(DanhMucGiayModel danhMucGiayModel);

    boolean deleteById(Long id);

    List<DanhMucDTO> getDanhMucs(String tenDanhMuc, String slug, Pageable pageable);

    Page<DanhMucDTO> locDanhMuc(String tenDanhMuc, String layMacDinh, Pageable pageable);
}
