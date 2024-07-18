package com.shoescms.common.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        authenticationException.printStackTrace();
        final String expired = (String) request.getAttribute("expired");
        if (expired == null)
            request.getRequestDispatcher("/v1/exception/entrypoint").forward(request, response);
        else
            request.getRequestDispatcher("/v1/exception/tokenexpired").forward(request, response);
    }
}