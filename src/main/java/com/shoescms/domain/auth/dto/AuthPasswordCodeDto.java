package com.shoescms.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "패스워드 변경")
@Data
public class AuthPasswordCodeDto {
    @Schema(description = "아이디", example = "1")
    private Long id;

    @Schema(description = "expire time", example = "2021030901")
    private String expire;

}
