package com.shoescms.domain.cart.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GioHangResDto {
    private Long id;
    private List<GioHangChiTietDto> sanPhamHienCo; // phan loai san pham hien so luong > 0
    private List<GioHangChiTietDto> sanPhamHetHang; // phan loai san pham hien so luong <= 0 || phan loai da xoa
}
