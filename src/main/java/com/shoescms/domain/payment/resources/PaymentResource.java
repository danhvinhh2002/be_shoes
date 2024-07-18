package com.shoescms.domain.payment.resources;

import com.shoescms.common.exception.ObjectNotFoundException;
import com.shoescms.common.security.JwtTokenProvider;
import com.shoescms.domain.payment.dtos.*;
import com.shoescms.domain.payment.services.PaymentService;
import com.shoescms.domain.payment.services.DonHangServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Slf4j
@Tag(name = "05. Payment")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/payment")
public class PaymentResource {
    private final DonHangServiceImpl donHangService;

    private final JwtTokenProvider jwtTokenProvider;

    private final PaymentService paymentService;


    @Operation(summary = "lay TT don hang", description = "lay TT don hang")
    @GetMapping(value = "detail/{id}")
    public ResponseEntity<DonHangDto> getTTDonHang(@PathVariable Long id) {
        return ResponseEntity.ok(donHangService.chiTietDonHang(id));
    }

    @PostMapping("dat-hang")
    public DonHangDto datHang(@RequestBody DatHangReqDto reqDto, @RequestHeader(name = "x-api-token", required = false) String xApiToken) throws UnsupportedEncodingException {
        if (xApiToken != null) // luu thong tin nguoi dat hang neu ho dang nhap
            reqDto.setNguoiTao(Long.parseLong(jwtTokenProvider.getUserPk(xApiToken)));
        DonHangDto dto = paymentService.datHang(reqDto);
        if(dto.getPhuongThucTT().equals(EPhuongThucTT.VNPAY))
            dto.setUrlPay(paymentService.taoUrlVnpay(dto));
        return dto;
    }
    
}
