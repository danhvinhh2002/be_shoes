package com.shoescms.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "인증 요청 정보")
@Data
@NoArgsConstructor
public class SignInReqDto {
    @Schema(description = "사용자 아이디", example = "test1")
    @NotBlank
    private String username;

    @Schema(description = "비밀번호", example = "test1")
    @NotBlank
    private String password;
}
