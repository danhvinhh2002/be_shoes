package com.shoescms.domain.cart.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GioHangChiTietResDto {
    private Long id;
    private Integer qty;
    private String anh;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private String variation; // ten bien the
    private Integer stockCnt;
}
