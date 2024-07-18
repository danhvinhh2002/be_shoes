package com.shoescms.domain.product.entitis;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "tbl_bien_the")
public class BienTheEntity {

    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(name = "ten_bien_the")
    private String tenBienThe;

}
