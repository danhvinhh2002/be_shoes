package com.shoescms.domain.cart.service;

import com.shoescms.domain.cart.dto.GioHangChiTietDto;
import com.shoescms.domain.cart.dto.GioHangChiTietResDto;
import com.shoescms.domain.cart.dto.GioHangResDto;
import com.shoescms.domain.cart.entity.GioHangEntity;
import com.shoescms.domain.cart.model.GioHangChiTietModel;
import com.shoescms.domain.product.entitis.SanPhamBienTheEntity;

import java.util.List;

public interface GioHangService {
    GioHangResDto findById(Long id);
    GioHangEntity findCartByUserId(Long nguoiDungEntity);

    List<GioHangChiTietResDto> gioHangCuaToi(Long nguoiDungEntity);

    GioHangEntity add(GioHangEntity gioHang);

    void remove(Long spBienTheId, Long userId);

    SanPhamBienTheEntity getBienTheBySanPhamId(Long sanPhamBienThe);

    GioHangChiTietDto addItem(GioHangChiTietModel model);

    List<GioHangChiTietResDto> dongBoGioHang(List<GioHangChiTietModel> models, Long userId);
}
