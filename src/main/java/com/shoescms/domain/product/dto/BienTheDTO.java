package com.shoescms.domain.product.dto;


import com.shoescms.domain.product.entitis.BienTheEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class BienTheDTO {
    private Long id;
    private String tenBienThe;

    public static BienTheDTO toDto(BienTheEntity bienTheEntity){
        if(bienTheEntity == null) return  null;
        return BienTheDTO
                .builder()
                .id(bienTheEntity.getId())
                .tenBienThe(bienTheEntity.getTenBienThe())
                .build();
    }
}
