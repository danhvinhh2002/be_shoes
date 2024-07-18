package com.shoescms.domain.product.models;


import com.shoescms.domain.product.enums.ELoaiBienThe;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SanPhamBienTheModel {
    private Long id;

    private Long sanPham;

    private Long bienThe1;

    private Long bienThe2;

    private Long bienTheGiaTri1;

    private Long bienTheGiaTri2;

    private Long anh;

    @NotNull
    private ELoaiBienThe loaiBienThe;
}
