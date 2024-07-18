package com.shoescms.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "패스워드 변경")
@Data
public class AuthChangePassDto {
    @Schema(description = "인증코드", example = "xxxxx")
    @NotBlank
    private String code;

    @Schema(description = "신규 비밀번호", example = "password")
    @NotBlank
    private String newPassword;
}
