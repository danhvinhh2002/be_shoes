package com.shoescms.domain.user.dto;

import com.shoescms.domain.user.entity.NguoiDungEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsermetaDto {
    private Long id;
    private String name;
    private String username;

    public static UsermetaDto toDto(NguoiDungEntity entity){
        if(entity == null) return null;
        return UsermetaDto
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getUsername())
                .build();
    }
}
