package com.shoescms.domain.user.dto;

import com.shoescms.common.enums.RoleEnum;
import com.shoescms.domain.user.entity.NguoiDungEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "사용자 등록 요청 정보")
@Data
@NoArgsConstructor
public class UserReqDto {
    @Schema(description = "ROLE", example = "ROLE_STORE_OWNER")
    @NotNull
    private RoleEnum role;

    @Schema(description = "사용자 아이디", example = "test01")
    @NotBlank
    private String userName;

    @Schema(description = "비밀번호", example = "password")
    @NotBlank
    private String password;

    @Schema(description = "이름", example = "이름")
    @NotBlank
    private String name;
    
    @Schema(description = "연락처", example = "010-1234-1234")
    @NotBlank
    private String phone;

    @Schema(description = "이메일", example = "example@seobuk.kr")
    private String email;

    public NguoiDungEntity toEntity() {
        return NguoiDungEntity.builder()
                .userName(userName)
                .name(name)
                .phone(phone)
                .email(email)
                .del(false)
                .build();
    }
}
