package com.shoescms.domain.payment.dtos;

import com.shoescms.domain.payment.entities.DonHangEntity;
import com.shoescms.domain.user.dto.UsermetaDto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonHangDto {
    private Long id;
    private String maDonHang;
    private Integer tongSp;
    private EPhuongThucTT phuongThucTT;
    private BigDecimal tongTienGiamGia;
    private BigDecimal tongGiaTien;
    private BigDecimal tongTienSp;
    private BigDecimal tongGiaCuoiCung;
    private BigDecimal phiShip;
    private UsermetaDto nguoiMua;
    private ETrangThaiDonHang trangThai;
    private LocalDateTime ngayTao;
    private DiaChiDto diaChi;
    private List<ChiTietDonHangDto> chiTietDonHang;
    private String ghiChu;
    private String urlPay;
    private Integer checkRate;

    private UsermetaDto nguoiCapNhat;
    public static DonHangDto toDto(DonHangEntity donHangEntity) {
        if (donHangEntity == null) return null;
        return DonHangDto.builder()
                .id(donHangEntity.getId())
                .maDonHang(donHangEntity.getMaDonHang())
                .tongSp(donHangEntity.getTongSp())
                .phuongThucTT(donHangEntity.getPhuongThucTT())
                .tongTienGiamGia(donHangEntity.getTongTienGiamGia())
                .tongGiaTien(donHangEntity.getTongGiaTien())
                .tongTienSp(donHangEntity.getTongTienSP())
                .phiShip(donHangEntity.getPhiShip())
                .tongGiaCuoiCung(donHangEntity.getTongGiaCuoiCung())
                .trangThai(donHangEntity.getTrangThai())
                .ngayTao(donHangEntity.getNgayTao())
                .diaChi(DiaChiDto.toDto(donHangEntity.getDiaChiEntity()))
                .chiTietDonHang(donHangEntity.getChiTietDonHangs().stream().map(ChiTietDonHangDto::toDto).collect(Collectors.toList()))
                .ghiChu(donHangEntity.getGhiChu())
                .checkRate(donHangEntity.getCheckRate())
                .build();
    }
}
