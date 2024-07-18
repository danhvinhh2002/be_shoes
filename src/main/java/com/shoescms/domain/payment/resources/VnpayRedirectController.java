package com.shoescms.domain.payment.resources;

import com.shoescms.domain.payment.dtos.VnpayRedirectReqDto;
import com.shoescms.domain.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("v1/vnpay")
@RequiredArgsConstructor
public class VnpayRedirectController {

    private final PaymentService paymentService;

    @RequestMapping("ket-qua")
    public String ketQuaThanhToanVnpay(VnpayRedirectReqDto reqDto) throws IOException {
        return paymentService.xuLyThanhToanVnpay(reqDto);
    }
}
