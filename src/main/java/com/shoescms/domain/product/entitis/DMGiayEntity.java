package com.shoescms.domain.product.entitis;


import com.shoescms.common.model.BaseDateEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_danh_muc_giay")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class DMGiayEntity extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.NVARCHAR)
    @Column(name = "ten_danh_muc", length =  255)
    private String tenDanhMuc;

    @Column(name = "slug", length = 255)
    private String slug;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_xoa")
    private LocalDateTime ngayXoa;
}
