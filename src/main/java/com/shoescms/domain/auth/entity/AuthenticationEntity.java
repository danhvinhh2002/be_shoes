package com.shoescms.domain.auth.entity;

import com.shoescms.common.model.BaseDateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_authentication")
public class AuthenticationEntity extends BaseDateEntity {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "NVARCHAR(128) COMMENT '아이디'")
    private String id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT '사용자 아이디'")
    private long userId;

    @Column(name = "access_token", nullable = false, columnDefinition = "NVARCHAR(256) COMMENT '발급 토큰'")
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, columnDefinition = "NVARCHAR(256) COMMENT '리프레쉬 토큰'")
    private String refreshToken;

    @Column(name = "has_revoked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE COMMENT '로그아웃 여부'")
    private Boolean hasRevoked;


}
