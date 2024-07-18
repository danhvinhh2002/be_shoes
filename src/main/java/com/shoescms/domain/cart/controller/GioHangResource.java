package com.shoescms.domain.cart.controller;


import com.shoescms.common.exception.ObjectNotFoundException;
import com.shoescms.common.security.JwtTokenProvider;
import com.shoescms.domain.cart.dto.GioHangChiTietDto;
import com.shoescms.domain.cart.dto.GioHangChiTietResDto;
import com.shoescms.domain.cart.entity.GioHangEntity;
import com.shoescms.domain.cart.model.GioHangChiTietModel;
import com.shoescms.domain.cart.service.GioHangService;
import com.shoescms.domain.product.entitis.SanPhamBienTheEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/gio-hang")
public class GioHangResource {

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public GioHangChiTietDto addItemToCart(
            @Parameter(required = true, description = "info items to add into cart")
            @RequestBody GioHangChiTietModel model,
            @Parameter(required = true, description = "access token to use API")
            @RequestHeader("x-api-token") String token
            ) {
        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));
        GioHangEntity gioHang = gioHangService.findCartByUserId(userId);
        SanPhamBienTheEntity sanPhamBienTheEntity = gioHangService.getBienTheBySanPhamId(model.getSanPhamBienThe());
        if(gioHang.getId() == null){
            gioHang = gioHangService.add(GioHangEntity.builder().nguoiDungId(userId).build());
            model.setGioHang(gioHang.getId());
        }else {
            if(sanPhamBienTheEntity.getSoLuong() > 0) {
                model.setGioHang(gioHang.getId());
                return gioHangService.addItem(model);
            }
            else if(sanPhamBienTheEntity.getSoLuong() == 0){
                throw new ObjectNotFoundException(50);
            }
        }
        return gioHangService.addItem(model);
    }

    @GetMapping("gio-hang-cua-toi")
    public List<GioHangChiTietResDto> viewCart(
            @RequestHeader("x-api-token") String token)
    {
        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));
        return gioHangService.gioHangCuaToi(userId);
    }

    @DeleteMapping("remove-item/{spBienTheId}")
    public void removeItemFromCart(@RequestHeader("x-api-token") String token,
            @PathVariable Long spBienTheId) {

        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));
        gioHangService.remove(spBienTheId, userId);
        }
        @PostMapping("dong-bo-gio-hang")
    public List<GioHangChiTietResDto> dongBoGioHang(@Parameter(required = true, description = "info items to add into cart")
                                      @RequestBody List<GioHangChiTietModel> models,
                              @Parameter(required = true, description = "access token to use API")
                                      @RequestHeader("x-api-token") String token){
            Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));
            return gioHangService.dongBoGioHang(models, userId);
    }

}
