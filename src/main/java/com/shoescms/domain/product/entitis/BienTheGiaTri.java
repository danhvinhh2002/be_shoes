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
@Table(name = "tbl_bien_the_gia_tri")
public class BienTheGiaTri {

    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bien_the_id", nullable = false, columnDefinition = "BIGINT COMMENT 'bien the gia tri'")
    private BienTheEntity bienThe;

    @Column(name = "gia_tri")
    private String giaTri;
}
