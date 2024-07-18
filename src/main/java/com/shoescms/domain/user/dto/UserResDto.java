package com.shoescms.domain.user.dto;

import com.shoescms.domain.product.enums.ESex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "사용자 조회 응답 정보")
@Data
@AllArgsConstructor
public class UserResDto {
    @Schema(description = "아이디", example = "1")
    private Long id;

    @Schema(description = "사용자 아이디", example = "test01")
    private String userId;

    @Schema(description = "이름", example = "이름")
    private String name;

    @Schema(description = "ROLE코드", example = "ROLE_MARKETING")
    private String roleCd;

    @Schema(description = "연락처", example = "010-1234-1234")
    private String phone;

    @Schema(description = "이메일", example = "example@seobuk.kr")
    private String email;

    @Schema(description = "sex", example = "MALE")
    private ESex sex;

    @Schema(description = "Birth date", example = "2023-10-01")
    private LocalDate birthDate;

    @Schema(description = "생성일자")
    private LocalDateTime createDate;

}
