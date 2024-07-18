package com.shoescms.domain.product.entitis;

import com.shoescms.domain.product.enums.ELoaiBienThe;
import com.shoescms.domain.product.enums.ESex;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_san_pham")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class SanPhamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ma_sp")
    private String maSP;

    @Column(name = "tieu_de")
    private String tieuDe;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "slug")
    private String slug;

    @Column(name = "gia_cu")
    private BigDecimal giaCu;

    @Column(name = "gia_moi")
    private BigDecimal giaMoi;

    @Column(name = "gioi_tinh")
    @Enumerated(EnumType.STRING)
    private ESex gioiTinh;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "thuong_hieu_id",nullable = false, columnDefinition = "BIGINT COMMENT 'Shoes thuong hieu'")
    private ThuongHieuEntity thuongHieu;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "danh_muc_id",nullable = false, columnDefinition = "BIGINT COMMENT 'Shoes danh muc'")
    private DMGiayEntity dmGiay;

    @Column(name = "nguoi_tao")
    private Long nguoiTao;

    @Column(name = "nguoi_cap_nhat")
    private Long nguoiCapNhat;

    @Column(name = "ngay_tao")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime ngayCapNhat;

    @Column(name = "ngay_xoa")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ngayXoa;

    @Column(name = "anh_chinh")
    private Long anhChinh;

    @Column(name = "anh_phu")
    private String anhPhu;

    @Column(name = "loaiBienThe")
    @Enumerated(EnumType.STRING)
    private ELoaiBienThe loaiBienThe;

    @Column(name = "hien_thi_web")
    private Boolean hienThiWeb;

    @Column(name = "tong_sp")
    @ColumnDefault("0")
    private Integer tongSp;

    @Column(name = "da_ban")
    @ColumnDefault("0")
    private Integer daBan;

    @Column(name = "tb_danh_gia")
    private Float tbDanhGia;

    @Column(name = "so_danh_gia")
    private Integer soDanhGia;

    @Column(name = "chat_lieu")
    private String chatLieu;

    @Column(name = "trong_luong")
    private String trongLuong;

    @Column(name = "cong_nghe")
    private String congNghe;

    @Column(name = "tinh_nang")
    private String tinhNang;

    @Column(name = "noi_san_xuat")
    private String noiSanXuat;
}
