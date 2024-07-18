package com.shoescms.domain.payment.dtos;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DatHangReqDto {
    @ArraySchema(schema = @Schema(defaultValue = "danh sach san pham dat hang trong gio hang", example = "1"))
    private List<Long> gioHangItemIds;

    @ArraySchema(schema = @Schema(description = "thong tin phan loai sp dat mua"))
    private List<GioHangTamThoiReqDto> gioHangTamThoiReqDto;

    @Schema(description = "dia chi nhan hang", example = "duong 32 ngo 214__My Dinh 2__Nam Tu Liem__Ha Noi")
    private String diaChiNhanHang;

    @Schema(description = "ho ten nguoi nhan hang", example = "Nguyen Van A")
    @NotNull
    private String hoTenNguoiNhan;

    @Schema(description = "So dien thoai nguoi nhan", example = "0584843998")
    @NotNull
    private String soDienThoaiNhanHang;

    @Schema(description = "Ghi chu", example = "giao luc 12h")
    private String note;

    @Schema(description = "mail", example = "mail@gmail.com")
    private String email;

    @Schema(description = "id cua dia chi da luu truoc do", example = "1")
    private Long diaChiId;

    // id cua nguoi dang nhap
    private Long nguoiTao;

    private Long maGiamGiaId;

    private BigDecimal shipFee;

    private BigDecimal totalPay;

    @Schema(description = "Ghi chu", example = "COD")
    private EPhuongThucTT phuongThucTT;

}
