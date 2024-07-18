package com.shoescms.domain.payment.dtos;

import com.shoescms.domain.payment.entities.ChiTietDonHangEntity;
import com.shoescms.domain.product.dto.SanPhamMetadataResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietDonHangDto {
    private Long id;
    private SanPhamMetadataResDto sanPham;
    private Long phanLoaiSpId;
    private Integer soLuong;
    private BigDecimal giaTien;
    private String motaPhanLoai;

    public static ChiTietDonHangDto toDto(ChiTietDonHangEntity chiTietDonHangEntity) {
        if (chiTietDonHangEntity == null) return null;
        return ChiTietDonHangDto.builder()
                .id(chiTietDonHangEntity.getId())
                .phanLoaiSpId(chiTietDonHangEntity.getPhanLoaiSpId())
                .soLuong(chiTietDonHangEntity.getSoLuong())
                .giaTien(chiTietDonHangEntity.getGiaTien())
                .motaPhanLoai(chiTietDonHangEntity.getMotaPhanLoai())
                .build();
    }
}
