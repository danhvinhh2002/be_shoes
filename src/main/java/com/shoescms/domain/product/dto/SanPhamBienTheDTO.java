package com.shoescms.domain.product.dto;

import com.shoescms.common.model.FileEntity;
import com.shoescms.domain.product.entitis.BienTheGiaTri;
import com.shoescms.domain.product.entitis.SanPhamBienTheEntity;
import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SanPhamBienTheDTO {
    private Long id;

    private Long bienThe1;

    private Long bienThe2;

    private Long giatri1;

    private Long giatri2;

    private FileEntity anh;
    private Integer soLuong;

    private BienTheDTO bienTheObj1;
    private BienTheDTO bienTheObj2;
    private BienTheGiaTriDTO giaTriObj1;
    private BienTheGiaTriDTO giaTriObj2;
    private SanPhamMetadataResDto sanPhamMetadata;
    private LocalDateTime ngayXoa;
    private FileEntity anhSpChinh;
    private String motaPhanLoai;

    public static SanPhamBienTheDTO toDTO(SanPhamBienTheEntity sanPhamBienTheEntity) {
        return
                SanPhamBienTheDTO.builder()
                        .id(sanPhamBienTheEntity.getId())
                        .bienThe1(sanPhamBienTheEntity.getBienThe1())
                        .bienThe2(sanPhamBienTheEntity.getBienThe2())
                        .giatri1(sanPhamBienTheEntity.getBienTheGiaTri1())
                        .giatri2(sanPhamBienTheEntity.getBienTheGiaTri2())
                        .soLuong(sanPhamBienTheEntity.getSoLuong())
                        .sanPhamMetadata(new SanPhamMetadataResDto(sanPhamBienTheEntity.getSanPham()))
                        .ngayXoa(sanPhamBienTheEntity.getNgayXoa())
                        .build();
    }

    public SanPhamBienTheDTO setAnh(FileEntity anh, FileEntity anhSpChinh) {
        this.anh = anh;
        this.anhSpChinh = anhSpChinh;
        return this;
    }

    public SanPhamBienTheDTO setGiaTriObj1(BienTheGiaTri giaTriObj1) {
        this.giaTriObj1 = BienTheGiaTriDTO.toDto(giaTriObj1);
        if (giaTriObj1 != null)
            this.bienTheObj1 = BienTheDTO.toDto(giaTriObj1.getBienThe());
        return this;
    }

    public SanPhamBienTheDTO setGiaTriObj2(BienTheGiaTri giaTriObj2) {
        this.giaTriObj2 = BienTheGiaTriDTO.toDto(giaTriObj2);
        if (giaTriObj2 != null)
            this.bienTheObj2 = BienTheDTO.toDto(giaTriObj2.getBienThe());
        return this;
    }

    public SanPhamBienTheDTO setMotaPhanLoai(String motaPhanLoai) {
        this.motaPhanLoai = motaPhanLoai;
        return this;
    }
}
