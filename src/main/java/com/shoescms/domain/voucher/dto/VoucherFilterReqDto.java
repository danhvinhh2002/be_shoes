package com.shoescms.domain.voucher.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VoucherFilterReqDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private EVoucherStatus status;
}
