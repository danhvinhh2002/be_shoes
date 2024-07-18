package com.shoescms.domain.cart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_gio_hang")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class GioHangEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_entity")
    private Long nguoiDungId;
}
