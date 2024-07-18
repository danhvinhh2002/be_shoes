package com.shoescms.domain.product.repository;

import com.shoescms.domain.product.entitis.SanPhamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISanPhamRepository extends JpaRepository<SanPhamEntity,Long>, JpaSpecificationExecutor<SanPhamEntity> {


    Page<SanPhamEntity> findAll(Pageable pageable);

    Page<SanPhamEntity> findByThuongHieuId(Long id, Pageable pageable);

    Page<SanPhamEntity> findByDmGiayId(Long id, Pageable pageable);

    Page<SanPhamEntity> findByThuongHieuIdAndDmGiayId(Long idThuongHieu, Long idDanhMucGiay, Pageable pageable);
}
