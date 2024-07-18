package com.shoescms.domain.product.dto;

import com.shoescms.common.model.FileEntity;
import com.shoescms.domain.product.entitis.SanPhamEntity;
import com.shoescms.domain.product.enums.ELoaiBienThe;
import com.shoescms.domain.product.enums.ESex;
import com.shoescms.domain.voucher.dto.VoucherMetadataDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class WebChiTietSanPhamDto {
    private Long id;

    private String maSP;

    private String tieuDe;

    private String moTa;

    private String slug;

    private BigDecimal giaCu;

    private BigDecimal giaMoi;

    private ESex gioiTinh;

    private ThuongHieuDTO thuongHieu;

    private DanhMucDTO dmGiay;

    private LocalDateTime ngayTao;

    private LocalDateTime ngayCapNhat;

    private FileEntity anhChinh;
    private List<FileEntity> anhPhu;

    private ELoaiBienThe loaiBienThe;
    private Boolean hienThiWeb;

    private List<SanPhamBienTheDTO> bienTheDTOS;
    private List<BienTheGiaTriDTO> giaTri1List;
    private List<BienTheGiaTriDTO> giaTri2List;

    private Integer tongSp;
    private Integer daBan;
    private Float tbDanhGia;
    private Integer soDanhGia;

    private List<VoucherMetadataDto> vouchers;

    private String chatLieu;
    private String trongLuong;
    private String congNghe;
    private String tinhNang;
    private String noiSanXuat;


    public static WebChiTietSanPhamDto toDto(SanPhamEntity sanPhamEntity) {
        if (sanPhamEntity == null) return null;
        return WebChiTietSanPhamDto.builder()
                .id(sanPhamEntity.getId())
                .maSP(sanPhamEntity.getMaSP())
                .tieuDe(sanPhamEntity.getTieuDe())
                .moTa(sanPhamEntity.getMoTa())
                .slug(sanPhamEntity.getSlug())
                .giaCu(sanPhamEntity.getGiaCu())
                .giaMoi(sanPhamEntity.getGiaMoi())
                .gioiTinh(sanPhamEntity.getGioiTinh())
                .thuongHieu(ThuongHieuDTO.toDTO(sanPhamEntity.getThuongHieu()))
                .dmGiay(DanhMucDTO.toDTO(sanPhamEntity.getDmGiay()))
                .ngayTao(sanPhamEntity.getNgayTao())
                .ngayCapNhat(sanPhamEntity.getNgayCapNhat())
                .loaiBienThe(sanPhamEntity.getLoaiBienThe())
                .hienThiWeb(sanPhamEntity.getHienThiWeb())
                .chatLieu(sanPhamEntity.getChatLieu())
                .trongLuong(sanPhamEntity.getTrongLuong())
                .congNghe(sanPhamEntity.getCongNghe())
                .tinhNang(sanPhamEntity.getTinhNang())
                .noiSanXuat(sanPhamEntity.getNoiSanXuat())
                .tongSp(sanPhamEntity.getTongSp())
                .tbDanhGia(sanPhamEntity.getTbDanhGia())
                .soDanhGia(sanPhamEntity.getSoDanhGia())
                .daBan(sanPhamEntity.getDaBan())
                .build();
    }


}
