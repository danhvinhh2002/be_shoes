package com.shoescms.domain.payment.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ThemMoiDonHangReqDto {

    @JsonIgnore
    private Long orderId;

    private List<GioHangTamThoiReqDto> phanLoaidIds;

    private Long nguoiDat;

    @Schema(description = "dia chi nhan hang", example = "duong 32 ngo 214__My Dinh 2__Nam Tu Liem__Ha Noi")
    private String diaChiNhanHang;

    @Schema(description = "ho ten nguoi nhan hang", example = "Nguyen Van A")
    @NotNull
    private String hoTenNguoiNhan;

    @Schema(description = "So dien thoai nguoi nhan", example = "0584843998")
    @NotNull
    private String soDienThoaiNhanHang;

    @Schema(description = "Ghi chu", example = "giao luc 12h")
    private String ghiChu;

    @Schema(description = "Ghi chu", example = "COD")
    private EPhuongThucTT phuongThucTT;

    private Long nguoiTao;
    private BigDecimal shipFee;
    private BigDecimal discount;
    private BigDecimal totalPay;
}
