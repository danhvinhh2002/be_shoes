package com.shoescms.domain.payment.repositories;

import com.shoescms.domain.payment.entities.ChiTietDonHangEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IChiTietDonHangRepository extends JpaRepository<ChiTietDonHangEntity, Long>, JpaSpecificationExecutor<ChiTietDonHangEntity> {
    @Query("SELECT e FROM ChiTietDonHangEntity e WHERE e.id IN :ids")
    List<ChiTietDonHangEntity> findByIds(@Param("ids") List<Long> ids);
}
