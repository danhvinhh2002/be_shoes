package com.shoescms.domain.cart.repository;

import com.shoescms.domain.cart.entity.GioHangEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IGioHangRepository extends JpaRepository<GioHangEntity,Long>, JpaSpecificationExecutor<GioHangEntity> {

    @Query("SELECT c FROM GioHangEntity c where c.nguoiDungId = :nguoiDungId")
    GioHangEntity findByNguoiDungId(@Param("nguoiDungId") Long nguoiDungEntity);
}
