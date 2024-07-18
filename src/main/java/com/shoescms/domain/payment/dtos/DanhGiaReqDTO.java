package com.shoescms.domain.payment.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DanhGiaReqDTO {
    private Long id;
    private Long donHangChiTietId;
    private Long nguoiTaoId;
    private String binhLuan;
    private Integer soSao;
    private LocalDateTime ngayTao;
}
