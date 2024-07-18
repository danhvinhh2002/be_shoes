package com.shoescms.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    ROLE_ADMIN(1L, "ROLE_ADMIN", "admin of store"),
    ROLE_STAFF(2L, "ROLE_STAFF", "staff"),
    ROLE_USER(3L, "ROLE_USER", "customer");

    private Long id;
    private String title;
    private String desc;
}
