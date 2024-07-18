package com.shoescms.domain.voucher.entity;

        import com.shoescms.domain.product.entitis.DMGiayEntity;
        import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;
        import org.hibernate.annotations.ColumnDefault;
        import org.hibernate.annotations.CreationTimestamp;
        import org.hibernate.annotations.UpdateTimestamp;

        import java.time.LocalDate;
        import java.time.LocalDateTime;
        import java.util.Date;
        import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_voucher")
@Entity
public class VoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ma_giam_gia", unique = true)
    private String maGiamGia;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "so_luong_da_dung")
    @ColumnDefault("0")
    private Integer soLuotDaDung;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiGiam", length = 10)
    private EGiamGiaTheo giamGiaTheo;

    @Column(name = "gia_giam", columnDefinition = "decimal default 0")
    private Double giaGiam;

    @Column(name = "pham_tram_giam", columnDefinition = "float default 0")
    private Float phanTramGiam;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_tao")
    @CreationTimestamp
    private LocalDateTime ngayTao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_cap_nhat")
    @UpdateTimestamp
    private LocalDateTime ngayCapNhat;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_xoa")
    private LocalDateTime ngayXoa;

    @Column(name = "nguoi_tao")
    private Long nguoiTao;

    @Column(name = "nguoi_cap_nhat")
    private Long nguoiCapNhat;

    @Column(name = "danh_muc_list")
    private String danhMucList;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_giam_gia")
    private ELoaiGiamGia loaiGiamGia;


}
