package com.shoescms.domain.product.entitis;

import com.shoescms.common.model.BaseDateEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_thuong_hieu")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class ThuongHieuEntity extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ten_thuong_hieu")
    private String tenThuongHieu;

    @Column(name = "slug")
    private String slug;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_xoa")
    private LocalDateTime ngayXoa;
}
