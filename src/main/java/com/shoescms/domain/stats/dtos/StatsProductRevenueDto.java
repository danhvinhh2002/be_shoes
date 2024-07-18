package com.shoescms.domain.stats.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatsProductRevenueDto {
    private Long id;
    private String tieuDe;
    private String anhChinh;
    private Integer soLuong;
    private BigDecimal total;
}
