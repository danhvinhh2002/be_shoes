package com.shoescms.domain.product.dto;

import com.shoescms.domain.product.enums.ESex;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SanPhamFilterReqDto {

    private String q;
    private String tieuDe;
    private String maSp;
    private String tinhTrangKho;
    private Long dmGiay;
    private Long thuongHieu;
    private Boolean hienThiWeb;
    private ESex gioiTinh;
    private Long mau;
    private Long sizeId;
    private Float soSaoDanhGia;
    private List<BigDecimal> khoangGia;
    private List<LocalDateTime> createdAtRange;
}
