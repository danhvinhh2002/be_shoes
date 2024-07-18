package com.shoescms.domain.product.dto;


import com.shoescms.domain.product.entitis.DMGiayEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhMucDTO {

    private  Long id;
    private String tenDanhMuc;
    private  String slug;
    private LocalDateTime ngayTao;
    private Integer soSp;


    public static DanhMucDTO toDTO(DMGiayEntity dm){
        if(dm ==null) return null;

        return
                DanhMucDTO.builder()
                        .id(dm.getId())
                        .tenDanhMuc(dm.getTenDanhMuc())
                        .slug(dm.getSlug())
                        .ngayTao(dm.getNgayTao())
                        .build();
    }

    public DanhMucDTO setSoSp(Integer soSp) {
        this.soSp = soSp;
        return this;
    }

    public static List<DanhMucDTO> convertToTDO(List<DMGiayEntity> dmGiayEntity){
        return dmGiayEntity.stream().map(s -> DanhMucDTO.builder()
                .id(s.getId())
                .tenDanhMuc(s.getTenDanhMuc())
                .slug(s.getSlug())
                .ngayTao(s.getNgayTao())
                .build()).collect(Collectors.toList());
    }

}
