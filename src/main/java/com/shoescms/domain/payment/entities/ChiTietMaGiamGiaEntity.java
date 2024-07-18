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
@Table(name = "tb_chi_tiet_ma_giam_gia")
public class ChiTietMaGiamGiaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "don_hang_id")
    private Long donHangId;

    @Column(name = "ma_giam_gia_id")
    private Long maGiamGiaId;

    @Column(name = "ngay_su_dung")
    private LocalDateTime ngaySuDung;
}
