package com.shoescms.domain.payment.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoescms.domain.payment.entities.DanhGiaEntity;
import com.shoescms.domain.user.dto.UsermetaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DanhGiaDto {

    private Long id;
    private String binhLuan;
    private Integer soSao;
    private LocalDateTime ngayTao;
    private UsermetaDto nguoiTao;

    private Boolean isHide;
    @JsonIgnore
    private Long nguoiTaoId;

    public static DanhGiaDto toDto(DanhGiaEntity danhGia){
        return DanhGiaDto
                .builder()
                .id(danhGia.getId())
                .binhLuan(danhGia.getBinhLuan())
                .soSao(danhGia.getSoSao())
                .ngayTao(danhGia.getNgayTao())
                .isHide(danhGia.getIsHide())
                .nguoiTaoId(danhGia.getNguoiTaoId())
                .build();
    }


}
