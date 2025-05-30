package com.ssafy.chaing.auth.jwt;

import com.ssafy.chaing.common.exception.AuthenticationException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(JwtProperties jwtProperties) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = jwtProperties.getAccessTokenExpiration();
        this.refreshTokenExpiration = jwtProperties.getRefreshTokenExpiration();
    }

    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 헤더에서 JWT 추출
     */
    public String extractTokenFromHeader(String header) {
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        return header.substring(TOKEN_PREFIX.length()).trim();
    }

    /**
     * Access Token 생성
     */
    public String createAccessToken(AuthClaims authClaims) {
        return createToken(authClaims, accessTokenExpiration);
    }

    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(AuthClaims authClaims) {
        return createToken(authClaims, refreshTokenExpiration);
    }

    /**
     * JWT 생성 메서드
     */
    private String createToken(AuthClaims authClaims, long expiration) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenExpiration = now.plusSeconds(expiration);

        return Jwts.builder()
                .claims()
                .add("subject", authClaims.getUserId())
                .and()
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(tokenExpiration.toInstant()))
                .signWith(key)
                .compact();
    }

    /**
     * JWT에서 User ID 추출
     */
    public Long getSubject(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("subject", Long.class);
    }

    /**
     * 토큰 만료 여부 확인
     */
    public boolean isTokenExpired(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 토큰 변조 여부 확인
     */
    public boolean isTokenManipulated(String token) {
        try {
            Jwts.parser()
                    .verifyWith(this.key)
                    .build()
                    .parseSignedClaims(token);
            return false;
        } catch (JwtException e) {
            log.error("토큰 검증 실패: {}", e.getMessage());
            return true;
        }
    }

    public AuthClaims extractClaimFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(this.key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return new AuthClaims(Long.valueOf(claims.get("subject").toString()));
        } catch (JwtException e) {
            log.error("토큰 검증 실패: {}", e.getMessage());
            throw new AuthenticationException(ExceptionCode.INVALID_TOKEN);
        }
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException e) {
            log.error("유효하지 않은 토큰: {}", e.getMessage());
            return false;
        }
    }
}
