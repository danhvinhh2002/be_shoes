package com.shoescms.domain.cart.entity;


import com.shoescms.common.model.BaseDateEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_gio_hang_chi_tiet")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class GioHangChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gio_hang")
    private Long gioHang;

    @Column(name = "san_pham_bien_the")
    private Long sanPhamBienThe;

    @Column(name = "so_luong")
    private Integer soLuong;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false, columnDefinition = "datetime(3) comment '수정일'")
    private LocalDateTime ngayCapNhat;
}
