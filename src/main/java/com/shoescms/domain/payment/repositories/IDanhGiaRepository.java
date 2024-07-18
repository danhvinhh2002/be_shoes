package com.shoescms.domain.payment.repositories;

import com.shoescms.domain.payment.entities.DanhGiaEntity;
import com.shoescms.domain.payment.entities.DonHangEntity;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDanhGiaRepository extends JpaRepository<DanhGiaEntity, Long> {

    @Query("SELECT e FROM DanhGiaEntity e WHERE e.donHangChiTietId IN :ids")
    List<DanhGiaEntity> findByIds(@Param("ids") List<Long> ids);

    @Query("select ROUND(AVG(dg.soSao), 1) as rate from DanhGiaEntity dg join ChiTietDonHangEntity ctdh " +
            "on ctdh.id = dg.donHangChiTietId " +
            "join SanPhamEntity sp on sp.id = ctdh.spId " +
            "where sp.id = ?1")
    Double findRatingBySanPham(Long idSanPham);

    @Query("select sp from SanPhamEntity sp join ChiTietDonHangEntity ctdh on sp.id = ctdh.spId " +
            "join DanhGiaEntity dg on dg.donHangChiTietId = ctdh.id " +
            "where dg.donHangChiTietId = ?1")
    SanPhamEntity findSanPhamDanhGia(Long idNguoiDanhGia);


    @Query("SELECT dh FROM DonHangEntity dh join ChiTietDonHangEntity ctdh on dh.id = ctdh.donHang " +
            "join DanhGiaEntity dg on dg.donHangChiTietId = ctdh.id " +
            "where dg.donHangChiTietId = ?1")
    DonHangEntity findDonHangDanhGia(Long id);
}
