package com.shoescms.domain.product.models;


import com.shoescms.domain.product.entitis.BienTheEntity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class BienTheGiaTriModel {
    private Long id;

    private BienTheEntity bienTheEntity;

    private String giaTri;
}
