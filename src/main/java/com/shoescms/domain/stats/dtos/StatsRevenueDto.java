package com.shoescms.domain.stats.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatsRevenueDto {

    private Object time;
    private BigDecimal total;
    private BigDecimal returned;
}
