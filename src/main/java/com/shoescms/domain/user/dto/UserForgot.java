package com.shoescms.domain.user.dto;

import com.shoescms.common.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserForgot {
    private RoleEnum role;
    private String userName;
    private String password;
    private String confirmPassword;
    private String name;
    private String phone;
    private String email;
    private String resetToken;
}
