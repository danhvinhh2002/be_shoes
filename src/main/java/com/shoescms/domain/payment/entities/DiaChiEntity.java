package com.shoescms.domain.payment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_dia_chi")
public class DiaChiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(name = "ten_nguoi_nhan", length = 100, nullable = false)
    private String tenNguoiNhan;

    @Column(name = "sdt", length = 15, nullable = false)
    private String sdt;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "dia_chi", length = 500, nullable = false)
    private String diaChi;

    @Column(name = "ngay_xoa")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayXoa;
}
