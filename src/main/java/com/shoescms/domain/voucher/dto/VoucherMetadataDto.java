package com.shoescms.domain.voucher.dto;

import com.shoescms.domain.voucher.entity.VoucherEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherMetadataDto {
    private Long id;
    private String maGiamGia;
    private String moTa;

    public static VoucherMetadataDto toDto(VoucherEntity entity){
        if(entity == null) return null;
        return VoucherMetadataDto
                .builder()
                .id(entity.getId())
                .maGiamGia(entity.getMaGiamGia())
                .moTa(entity.getMoTa())
                .build();
    }
}
