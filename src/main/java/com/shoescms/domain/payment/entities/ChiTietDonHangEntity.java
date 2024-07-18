package com.shoescms.domain.payment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_chi_tiet_don_hang")
public class ChiTietDonHangEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(name = "don_hang_id", nullable = false)
    private Long donHang;

    @Column(name = "phan_loai_sp_id", nullable = false)
    private Long phanLoaiSpId;

    @Column(name = "sp_id", nullable = false)
    private Long spId;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "gia_tien", nullable = false)
    private BigDecimal giaTien;

    @Column(name = "mo_ta_pl")
    private String motaPhanLoai;

    @Column(name = "ngay_xoa")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ngayXoa;

}
