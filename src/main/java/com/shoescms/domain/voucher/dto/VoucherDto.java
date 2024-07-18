package com.shoescms.domain.voucher.dto;

import com.shoescms.domain.product.dto.DanhMucDTO;
import com.shoescms.domain.user.dto.UsermetaDto;
import com.shoescms.domain.voucher.entity.EGiamGiaTheo;
import com.shoescms.domain.voucher.entity.ELoaiGiamGia;
import com.shoescms.domain.voucher.entity.VoucherEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoucherDto {

    private Long id;

    private String maGiamGia;

    private String moTa;

    private Integer soLuotDaDung;

    private EGiamGiaTheo giamGiaTheo;

    private Double giaGiam;

    private Float phanTramGiam;

    private LocalDate ngayBatDau;

    private LocalDate ngayKetThuc;

    private LocalDateTime ngayTao;

    private LocalDateTime ngayCapNhat;

    private UsermetaDto nguoiTao;

    private UsermetaDto nguoiCapNhat;

    private String danhMucList;

    private ELoaiGiamGia loaiGiamGia;

    public static VoucherDto toDto(VoucherEntity entity){
        return VoucherDto
                .builder()
                .id(entity.getId())
                .maGiamGia(entity.getMaGiamGia())
                .moTa(entity.getMoTa())
                .soLuotDaDung(entity.getSoLuotDaDung())
                .giamGiaTheo(entity.getGiamGiaTheo())
                .giaGiam(entity.getGiaGiam())
                .phanTramGiam(entity.getPhanTramGiam())
                .ngayBatDau(entity.getNgayBatDau())
                .ngayKetThuc(entity.getNgayKetThuc())
                .ngayTao(entity.getNgayTao())
                .ngayCapNhat(entity.getNgayCapNhat())
                .danhMucList(entity.getDanhMucList())
                .loaiGiamGia(entity.getLoaiGiamGia())
                .build();
    }
}
