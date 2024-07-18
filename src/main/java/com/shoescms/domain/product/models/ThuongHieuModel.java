package com.shoescms.domain.product.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThuongHieuModel {
    private  Long id;

    private String tenThuongHieu;

//    private  String slug;
}
