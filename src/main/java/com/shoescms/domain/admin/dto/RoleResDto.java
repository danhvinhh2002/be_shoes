package com.shoescms.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "ROLE 조회 응답 정보")
@Data
public class RoleResDto {
    @Schema(description = "ROLE 코드", example = "ROLE_OPERATION")
    private String roleCd;

    public RoleResDto(String roleCd ) {
        this.roleCd = roleCd;
    }
}
