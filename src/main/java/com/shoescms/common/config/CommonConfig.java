package com.shoescms.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CommonConfig {
    @Value("${shoescms.vnpayredirect}")
    private String vnpayRedirectURl;

    @Value("${shoescms.forgotPassword}")
    private String forgotPass;
}
