package com.shoescms.domain.voucher.dto;

import com.shoescms.domain.voucher.entity.EGiamGiaTheo;
import com.shoescms.domain.voucher.entity.ELoaiGiamGia;
import com.shoescms.domain.voucher.entity.VoucherEntity;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VoucherReqDto {
    private Long id;

    private String maGiamGia;

    private String moTa;

    private Integer soLuotDaDung;

    private EGiamGiaTheo giamGiaTheo;

    private Double giaGiam;

    private Float phanTramGiam;

    private LocalDate ngayBatDau;

    private LocalDate ngayKetThuc;

    private List<Long> danhMucList;

    private ELoaiGiamGia loaiGiamGia;
    public VoucherEntity toEntity(){
        return VoucherEntity
                .builder()
                .id(this.getId())
                .maGiamGia(this.getMaGiamGia())
                .moTa(this.getMoTa())
                .soLuotDaDung(this.getSoLuotDaDung())
                .giamGiaTheo(this.getGiamGiaTheo())
                .giaGiam(this.getGiaGiam())
                .phanTramGiam(phanTramGiam)
                .ngayBatDau(ngayBatDau)
                .ngayKetThuc(ngayKetThuc)
                .danhMucList(danhMucList != null ? String.join(",", danhMucList.stream().map(Object::toString).toList()) : null)
                .loaiGiamGia(loaiGiamGia)
                .build();
    }
}
