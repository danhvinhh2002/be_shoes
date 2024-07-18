package com.shoescms.domain.payment.dtos;

import lombok.Getter;

@Getter
public enum ETrangThaiDonHang {
    WAITING_CONFIRM, VNPAY_PAID, DELIVERING, COMPLETED, PHONE_RETURNED, WRONG_SP_RETURNED, DRAFT, CANCELLED
}
