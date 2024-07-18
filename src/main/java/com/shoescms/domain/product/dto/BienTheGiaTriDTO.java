package com.shoescms.domain.product.dto;


import com.shoescms.domain.product.entitis.BienTheGiaTri;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class BienTheGiaTriDTO {
    private Long id;

    private String giaTri;

    private List<BienTheGiaTriDTO> bienThe2;

    public BienTheGiaTriDTO(Long id, String giaTri) {
        this.id = id;
        this.giaTri = giaTri;
    }

    public static BienTheGiaTriDTO toDto(BienTheGiaTri bienTheGiaTri) {
        if(bienTheGiaTri == null) return null;
        return BienTheGiaTriDTO
                .builder()
                .id(bienTheGiaTri.getId())
                .giaTri(bienTheGiaTri.getGiaTri())
                .build();
    }
}
