package com.shoescms.domain.payment.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class LocDonHangReqDto {
    private EPhuongThucTT phuongThucTT;
    private ETrangThaiDonHang trangThai;
    private LocalDateTime startOrderDate;
    private LocalDateTime endOrderDate;
    private String maDonHang;
    private String tenNguoiNhan;
    private String tenSanPham;
}
