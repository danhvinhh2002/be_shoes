package com.shoescms.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseDateEntity {
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, columnDefinition = "datetime(3) comment '생성일'")
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat", nullable = false, columnDefinition = "datetime(3) comment '수정일'")
    private LocalDateTime ngayCapNhat;
}