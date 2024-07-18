package com.shoescms.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(description = "인증 응답 정보")
@Data
@Builder
public class SignInResDto {
    @Schema(description = "사용자 아이디", example = "test1")
    private String userName;

    @Schema(description = "사용자 role", example = "ADMIN")
    private List<String> roles;

    @Schema(description = "access token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiUk9MRV9TVVBFUl9BRE1JTiJdLCJpYXQiOjE2ODAxNzI0MDQsImV4cCI6MTY4MDI1ODgwNH0.gTech9LAvdaeaxFDiN6B8xnW1VdGNi4A_Ha17Rjquvc")
    private String accessToken;

    @Schema(description = "access token expire in", example = "Fri Mar 31 19:33:24 KST 2023")
    private String accessExpireIn;

    @Schema(description = "refresh token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiUk9MRV9TVVBFUl9BRE1JTiJdLCJpYXQiOjE2ODAxNzI0MDQsImV4cCI6MTY4Mjc2NDQwNH0.Vqw74gwMjJwi-pVgWq-C6eNM3MM755eUu5ZPOBwsJBI")
    private String refreshToken;

    @Schema(description = "refresh token expire in", example = "Sat Apr 29 19:33:24 KST 2023")
    private String refreshExpireIn;

    @Schema(description = "패스워드 설정 완료 여부")
    private Boolean setPassword;
}
