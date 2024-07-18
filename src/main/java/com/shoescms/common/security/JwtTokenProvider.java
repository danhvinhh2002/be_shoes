package com.shoescms.common.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    @Value("spring.jwt.secret")
    private String secretKey;
    private final long tokenExpire = 1000L * 60 * 60 * 24 * 30; // 24시간만 토큰 유효
    private final long refreshTokenExpire = 1000L * 60 * 60 * 24 * 30; // 30일만 refresh 토큰 유효
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
    public String createToken(String userPk, List<String> roles, StringBuilder expire) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + tokenExpire);
        expire.append(expireTime);

        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(expireTime) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    public String createRefreshToken(String userPk, List<String> roles, StringBuilder expire) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + refreshTokenExpire);
        expire.append(expireTime);

        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(expireTime) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    // Jwt 임시 토큰 생성
    public String createTokenByExpire(String userPk, List<String> roles, Long expire) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + expire);
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(expireTime) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean hasRole(String token, String input) {
        List<String> roles = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("roles", List.class);
        for (String role : roles) {
            if (role.equalsIgnoreCase(input))
                return true;
        }
        return false;
    }

    // Request의 Header에서 token 파싱 : "x-api-token: jwt토큰"
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("x-api-token");
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Date now = new Date();
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);

            Date expire = claims.getBody().getExpiration();
            if (expire.before(now)) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                log.error("token expire : expire({})/now({})", dateFormat.format(expire), dateFormat.format(now));
            }
            return !expire.before(now);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다. : {}({})", jwtToken, e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다. : {}({})", jwtToken, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다. : {}({})", jwtToken, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다. : {}({})", jwtToken, e.getMessage());
        } catch (Exception e) {
            log.error("invalid token : {}({})", jwtToken, e.getMessage());
        }
        return false;
    }
}