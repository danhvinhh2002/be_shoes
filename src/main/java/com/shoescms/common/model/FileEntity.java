package com.shoescms.common.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(length = 500, nullable = false)
    private String url;

    @Column(length = 500, nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private Boolean isVerified;

    public FileEntity(String url, String name, String path) {
        this.url = url;
        this.path = path;
        this.name = name;
        isVerified = false;
    }

}
