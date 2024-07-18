package com.shoescms.domain.product.dto;

import com.shoescms.common.model.FileEntity;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamMetadataResDto {
    private Long id;
    private String tieuDe;
    private BigDecimal giaMoi;
    private BigDecimal giaCu;
    private FileEntity anhChinh;

    public SanPhamMetadataResDto(SanPhamEntity sanPhamEntity) {
        this.id = sanPhamEntity.getId();
        this.tieuDe = sanPhamEntity.getTieuDe();
        this.giaMoi = sanPhamEntity.getGiaMoi();
        this.giaCu = sanPhamEntity.getGiaCu();
    }

    public static SanPhamMetadataResDto toDto(SanPhamEntity sanPhamEntity) {
        if (sanPhamEntity == null) return null;
        return SanPhamMetadataResDto
                .builder()
                .id(sanPhamEntity.getId())
                .tieuDe(sanPhamEntity.getTieuDe())
                .giaMoi(sanPhamEntity.getGiaMoi())
                .giaCu(sanPhamEntity.getGiaCu())
                .build();
    }
}
