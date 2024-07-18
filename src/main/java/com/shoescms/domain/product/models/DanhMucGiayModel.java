package com.shoescms.domain.product.models;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhMucGiayModel {

    private Long id;

    private String tenDanhMuc;

//    private String slug;
}
