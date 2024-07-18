package com.shoescms.domain.payment.dtos;

import com.shoescms.domain.payment.entities.DiaChiEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaChiDto {
    private Long id;
    private String tenNguoiNhan;
    private String sdt;
    private String diaChi;

    public static DiaChiDto toDto(DiaChiEntity diaChiEntity) {
        if (diaChiEntity == null) return null;
        return DiaChiDto.builder()
                .id(diaChiEntity.getId())
                .tenNguoiNhan(diaChiEntity.getTenNguoiNhan())
                .sdt(diaChiEntity.getSdt())
                .diaChi(diaChiEntity.getDiaChi())
                .build();
    }
}
