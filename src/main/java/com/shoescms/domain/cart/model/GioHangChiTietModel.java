package com.shoescms.domain.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class GioHangChiTietModel {

    private Long id;

    private Long gioHang;

    private Long sanPhamBienThe;

    private Integer soLuong;
}
