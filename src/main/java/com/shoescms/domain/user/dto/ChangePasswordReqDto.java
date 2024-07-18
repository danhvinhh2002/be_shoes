package com.shoescms.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "비밀번호 변경 요청 정보")
@Data
public class ChangePasswordReqDto {
    @Schema(description = "이전 비밀번호", example = "password")
    @NotBlank
    private String oldPassword;

    @Schema(description = "신규 비밀번호", example = "password")
    @NotBlank
    private String newPassword;

    @Schema(description = "신규 비밀번호 확인", example = "password")
    @NotBlank
    private String newPasswordConfirm;
}
