package com.shoescms.domain.product.dto;

import com.shoescms.common.model.FileEntity;
import com.shoescms.domain.product.entitis.ThuongHieuEntity;
import com.shoescms.domain.product.enums.ELoaiBienThe;
import com.shoescms.domain.product.enums.ESex;
import com.shoescms.domain.product.entitis.DMGiayEntity;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import com.shoescms.domain.user.dto.UsermetaDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamDto {
    private Long id;

    private String maSP;

    private String tieuDe;

    private String moTa;

    private String slug;

    private BigDecimal giaCu;

    private BigDecimal giaMoi;

    private ESex gioiTinh;


    private ThuongHieuEntity thuongHieu;

    private DMGiayEntity dmGiay;

    private UsermetaDto nguoiTao;

    private Long nguoiCapNhat;

    private LocalDateTime ngayTao;

    private LocalDateTime ngayCapNhat;

    private LocalDateTime ngayXoa;
    private FileEntity anhChinh;
    private List<FileEntity> anhPhu;

    private ELoaiBienThe loaiBienThe;
    private Boolean hienThiWeb;
    private Integer tongSp;
    private Float tbDanhGia;
    private Integer soDanhGia;

    private String chatLieu;
    private String trongLuong;
    private String congNghe;
    private String tinhNang;
    private String noiSanXuat;

    public static SanPhamDto toDto(SanPhamEntity sanPhamEntity) {
        if (sanPhamEntity == null) return null;
        return SanPhamDto.builder()
                .id(sanPhamEntity.getId())
                .maSP(sanPhamEntity.getMaSP())
                .tieuDe(sanPhamEntity.getTieuDe())
                .moTa(sanPhamEntity.getMoTa())
                .slug(sanPhamEntity.getSlug())
                .giaCu(sanPhamEntity.getGiaCu())
                .giaMoi(sanPhamEntity.getGiaMoi())
                .gioiTinh(sanPhamEntity.getGioiTinh())
                .thuongHieu(sanPhamEntity.getThuongHieu())
                .dmGiay(sanPhamEntity.getDmGiay())
                .nguoiCapNhat(sanPhamEntity.getNguoiCapNhat())
                .ngayTao(sanPhamEntity.getNgayTao())
                .ngayCapNhat(sanPhamEntity.getNgayCapNhat())
                .ngayXoa(sanPhamEntity.getNgayXoa())
                .loaiBienThe(sanPhamEntity.getLoaiBienThe())
                .hienThiWeb(sanPhamEntity.getHienThiWeb())
                .tongSp(sanPhamEntity.getTongSp())
                .chatLieu(sanPhamEntity.getChatLieu())
                .trongLuong(sanPhamEntity.getTrongLuong())
                .congNghe(sanPhamEntity.getCongNghe())
                .tinhNang(sanPhamEntity.getTinhNang())
                .noiSanXuat(sanPhamEntity.getNoiSanXuat())
                .tongSp(sanPhamEntity.getTongSp())
                .tbDanhGia(sanPhamEntity.getTbDanhGia())
                .soDanhGia(sanPhamEntity.getSoDanhGia())
                .build();
    }

    public SanPhamDto setAnhChinh(FileEntity anhChinh) {
        this.anhChinh = anhChinh;
        return this;
    }

    public SanPhamDto setNguoiTao(UsermetaDto nguoiTao) {
        this.nguoiTao = nguoiTao;
        return  this;
    }
}
