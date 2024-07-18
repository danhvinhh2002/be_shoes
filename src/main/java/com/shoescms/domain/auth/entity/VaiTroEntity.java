package com.shoescms.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "tb_role")
public class VaiTroEntity {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '아이디'")
    private Long id;


    @Column(name = "role_cd", nullable = false, unique = true, columnDefinition = "NVARCHAR(32) COMMENT 'ROLE 코드'")
    private String roleCd;
}
