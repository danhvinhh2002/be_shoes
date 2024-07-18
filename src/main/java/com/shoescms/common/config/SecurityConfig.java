package com.shoescms.common.config;

import com.shoescms.common.enums.RoleEnum;
import com.shoescms.common.security.CustomAccessDeniedHandler;
import com.shoescms.common.security.CustomAuthenticationEntryPoint;
import com.shoescms.common.security.JwtAuthenticationFilter;
import com.shoescms.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(PERMIT_ALL_LIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private static final String[] PERMIT_ALL_LIST = {
            "/*/auth/**",
            "/*/user/sign-up",
            "/*/user/find-id",
            "/v1/user/find-pw/**",
            "/*/user/{id}/password",
            "/*/kiosk/seq",
            "/*/file/**",
            "/*/etc/code",
            "/*/etc/health-check",
            "/v1/etc/**",
            "/api/cart/**",
            "/cart/**",
            "/v1/coupon/**",
            "/v1/payment/dat-hang",
            "/v1/vnpay/ket-qua",
            "/v1/payment/detail/**",
            "/v1/don-hang/chi-tiet/**",
            "/v1/don-hang/public/**",
            "/v1/san-pham/public/**",
            "/v1/san-pham/public/get-all-gia-tri-bien-the/",
            "/v1/danh-muc-giay/loc-danh-muc/**",
            "/v1/thuong-hieu/loc-thuong-hieu/**",
            "/v1/user/token",
            "/v1/user/forgot-pass/**",
            "/v1/voucher/public/**",
            "/v1/danh-gia/public/**",
            "/v1/danh-gia/soSao/**"

    };
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(IGNORING_LIST);
    }

    private static final String[] IGNORING_LIST = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger/**",
            "/actuator/health",
            "/*/exception/**"
    };

    private WebExpressionAuthorizationManager getWebExpressionAuthorizationManager(final String expression) {
        final var expressionHandler = new DefaultHttpSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        final var authorizationManager = new WebExpressionAuthorizationManager(expression);
        authorizationManager.setExpressionHandler(expressionHandler);
        return authorizationManager;
    }

    CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("https://*");
        configuration.addAllowedOriginPattern("http://*:*");
        configuration.addAllowedOriginPattern("https://*:*");
        configuration.addAllowedOriginPattern("*");

        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}