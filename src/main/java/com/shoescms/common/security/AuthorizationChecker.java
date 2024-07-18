package com.shoescms.common.security;

import com.shoescms.common.enums.RoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationChecker {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    String checkToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            request.setAttribute("expired", "expired");
            return null;
        }

        if (!jwtTokenProvider.validateToken(token)) {
            request.setAttribute("expired", "expired");
            return null;
        }

        return token;
    }

    public Boolean hasAdminOrCheckId(HttpServletRequest request, Long id) {
        log.info("======================================================================");
        log.info("userid check : {}", request.getRequestURI());
        log.info("======================================================================");
        String token = checkToken(request);
        if (token == null)
            return false;
        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));

        // task: need update
        // check admin or same user
//        if (!jwtTokenProvider.hasRole(token, RoleEnum.ROLE_ADMIN.name()) && !jwtTokenProvider.hasRole(token, RoleEnum.ROLE_SUPER_ADMIN.name())) {
//            return id.equals(userId);
//        }
        return true;
    }

    public Boolean hasAuthorityOrOwner(HttpServletRequest request, Long id, String authority) {
        log.info("======================================================================");
        log.info("userid check : {}", request.getRequestURI());
        log.info("======================================================================");
        String token = checkToken(request);
        if (token == null)
            return false;
        Long userId = Long.valueOf(jwtTokenProvider.getUserPk(token));

        // check hasAuthority or same user
        List<String> grantedAuthority = userDetailsService.loadUserByUsername(jwtTokenProvider.getUserPk(token)).getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (!grantedAuthority.contains(authority)) {
            return id.equals(userId);
        }
        return true;
    }
}
