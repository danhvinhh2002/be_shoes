package com.shoescms.domain.payment.repositories;

import com.shoescms.domain.payment.dtos.ETrangThaiDonHang;
import com.shoescms.domain.payment.entities.DonHangEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDonHangRepository extends JpaRepository<DonHangEntity, Long>, JpaSpecificationExecutor<DonHangEntity> {
    DonHangEntity findByMaDonHang(String vnpTxnRef);

    @Query(value = "select dh from DonHangEntity dh where dh.nguoiMuaId = ?1 and dh.trangThai = ?2")
    Page<DonHangEntity> findByNguoiMuaId(Long nguoiMuaId, ETrangThaiDonHang trangThai, Pageable pageable);
    @Query(value = "select dh from DonHangEntity dh where dh.nguoiMuaId = ?1")
    Page<DonHangEntity> findByNguoiMuaId(Long nguoiMuaId, Pageable pageable);

}
