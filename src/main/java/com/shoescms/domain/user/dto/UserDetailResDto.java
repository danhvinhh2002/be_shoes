package com.shoescms.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "사용자 상세 조회 응답 정보")
@Data
@AllArgsConstructor
public class UserDetailResDto {
    @Schema(description = "아이디", example = "1")
    private Long id;

    @Schema(description = "사용자 아이디", example = "test01")
    private String userId;

    @Schema(description = "이름", example = "이름")
    private String name;

    @Schema(description = "ROLE코드", example = "ROLE_MARKETING")
    private String roleCd;

    @Schema(description = "ROLE명칭(한글)", example = "마케팅팀")
    private String roleNmKr;

    @Schema(description = "ROLE명칭(영문)", example = "Marketing team")
    private String roleNmEn;

    @Schema(description = "연락처", example = "010-1234-1234")
    private String phone;

    @Schema(description = "이메일", example = "example@seobuk.kr")
    private String email;

    @Schema(description = "생성일자")
    private LocalDateTime createDate;
}
