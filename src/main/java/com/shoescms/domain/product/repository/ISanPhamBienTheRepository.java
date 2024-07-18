package com.shoescms.domain.product.repository;


import com.shoescms.domain.product.entitis.SanPhamBienTheEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISanPhamBienTheRepository extends JpaRepository<SanPhamBienTheEntity, Long>, JpaSpecificationExecutor<SanPhamBienTheEntity> {
    @Modifying
    void deleteAllBySanPhamId(Long spId);
    List<SanPhamBienTheEntity> findAllAllBySanPhamIdAndNgayXoaIsNull(Long spId);
}
