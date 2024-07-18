package com.shoescms.domain.cart.repository;

import com.shoescms.domain.cart.entity.GioHangChiTiet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IGioHangChiTietRepository extends JpaRepository<GioHangChiTiet,Long>, JpaSpecificationExecutor<GioHangChiTiet> {

    @Query("SELECT c FROM GioHangChiTiet c WHERE c.gioHang = :gioHang AND c.sanPhamBienThe = :sanPhamBienThe")
    GioHangChiTiet findByGioHangAndSanPhamBienThe(
            @Param("gioHang") Long gioHang,
            @Param("sanPhamBienThe") Long sanPhamBienThe);

    List<GioHangChiTiet> findAllByGioHang(Long id, Sort by);

    @Modifying
    @Query("delete from GioHangChiTiet c where c.sanPhamBienThe in ?1 and c.gioHang = ?2")
    void deleteItemFromCart(List<Long> spBt, Long gioHang);
}
