package com.shoescms.domain.payment.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_danh_gia")
public class DanhGiaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "don_hang_chi_tiet_id")
    private Long donHangChiTietId;

    @Column(name = "nguoi_tao_id")
    private Long nguoiTaoId;

    @Column(name = "binh_luan", length = 255)
    private String binhLuan;

    @Column(name = "so_sao")
    private Integer soSao;

    @Column(name = "is_hide")
    private Boolean isHide;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
}
