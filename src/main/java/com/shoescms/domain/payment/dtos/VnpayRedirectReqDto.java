package com.shoescms.domain.payment.dtos;

import lombok.Data;

@Data
public class VnpayRedirectReqDto {

    private String vnp_BankCode;
    private String vnp_CardType;
    private String vnp_OrderInfo;
    private String vnp_PayDate;
    private String vnp_ResponseCode;
    private String vnp_TransactionNo;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef; // maDonHang
}
