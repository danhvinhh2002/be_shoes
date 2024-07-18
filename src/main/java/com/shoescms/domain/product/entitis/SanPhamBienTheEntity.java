package com.shoescms.domain.product.entitis;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_san_pham_bien_the")
public class SanPhamBienTheEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "san_pham_id", nullable = false, columnDefinition = "BIGINT COMMENT 'san pham '")
    private SanPhamEntity sanPham;

    @Column(name = "bien_the_id1", columnDefinition = "BIGINT COMMENT 'bien the '")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long bienThe1;

    @Column(name = "bien_the_id2",  columnDefinition = "BIGINT COMMENT 'bien the '")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long bienThe2;

    @Column(name = "gia_tri_bt_id1", columnDefinition = "BIGINT COMMENT 'gia tri bien the '")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long bienTheGiaTri1;

    @Column(name = "gia_tri_bt_id2", columnDefinition = "BIGINT COMMENT 'gia tri bien the '")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long bienTheGiaTri2;

    @Column(name = "anh")
    private Long anh;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "mo_ta_pl")
    private String motaPhanLoai;

    @Column(name = "ngay_xoa")
    private LocalDateTime ngayXoa;


    public SanPhamBienTheEntity delete(){
        this.ngayXoa = LocalDateTime.now();
        return this;
    }

}
