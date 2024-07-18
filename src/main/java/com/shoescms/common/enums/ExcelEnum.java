package com.shoescms.common.enums;

import com.shoescms.domain.user.repository.UserQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExcelEnum {
    XLSX001("내부직원 계정 조회", UserQueryRepository.class, "exportStaffUserList", String.class, null),
    XLSX002("상점 계정 조회", UserQueryRepository.class, "exportStoreUserList", String.class, null);
    private String desc;
    private Class<?> repoClazz;
    private String queryId;
    private Class<?> reqClazz;
    private String key;
}
