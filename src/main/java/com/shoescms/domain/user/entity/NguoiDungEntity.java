package com.shoescms.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shoescms.common.model.BaseDateEntity;
import com.shoescms.domain.auth.entity.VaiTroEntity;
import com.shoescms.domain.product.enums.ESex;
import com.shoescms.domain.user.dto.UserUpdateReqDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_user")
public class NguoiDungEntity extends BaseDateEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '아이디'")
    private Long id;
    
    @Column(name = "user_name", nullable = false, columnDefinition = "NVARCHAR(20) COMMENT '사용자 아이디'")
    private String userName;
    
    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(20) COMMENT '이름'")
    private String name;

    @Column(name = "phone", columnDefinition = "NVARCHAR(30) COMMENT '연락처'")
    private String phone;

    @Column(name = "email", columnDefinition = "NVARCHAR(64) COMMENT '이메일'")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private ESex sex;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "password", columnDefinition = "NVARCHAR(128) COMMENT '비밀번호'")
    private String password;

    @Column(name = "tmp_password", columnDefinition = "NVARCHAR(128) COMMENT '임시 비밀번호'")
    private String tmpPassword;

    @Column(name = "approved", columnDefinition = "BOOLEAN DEFAULT FALSE COMMENT '승인여부'")
    private Boolean approved;

    @Column(name = "del", columnDefinition = "BOOLEAN DEFAULT FALSE COMMENT '삭제여부'")
    private Boolean del;

    @Column(name = "delete_date", columnDefinition = "DATETIME(3) COMMENT '삭제일'")
    private LocalDateTime deleteDate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false, columnDefinition = "BIGINT COMMENT 'user role'")
    private VaiTroEntity role;

    @Transient
    @Setter
    private Collection<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.userName;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setTmpPassword(String password) {
        this.tmpPassword = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void update(UserUpdateReqDto reqDto) {
        if (reqDto.getName() != null)           this.name = reqDto.getName();
        if (reqDto.getBirthDate() != null)           this.birthDate = reqDto.getBirthDate();
        if (reqDto.getSex() != null)           this.sex = reqDto.getSex();
        if (reqDto.getPhone() != null)          this.phone = reqDto.getPhone();
        if (reqDto.getEmail() != null)          this.email = reqDto.getEmail();
    }
    public void update(NguoiDungEntity reqDto) {
        if (reqDto.getPassword() != null)       this.password = reqDto.getPassword();
    }

    public void setDel() {
        this.del = true;
        this.deleteDate = LocalDateTime.now();
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
