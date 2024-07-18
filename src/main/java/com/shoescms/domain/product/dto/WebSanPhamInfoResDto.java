package com.shoescms.domain.product.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WebSanPhamInfoResDto {
    private Long id;

    private String maSP;

    private String tieuDe;

    private String moTa;

    private String slug;

    private BigDecimal giaCu;

    private BigDecimal giaMoi;
}
