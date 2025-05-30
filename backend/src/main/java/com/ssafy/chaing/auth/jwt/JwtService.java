package com.ssafy.chaing.auth.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    public String extractTokenFromHeader(String header) {
        return jwtUtil.extractTokenFromHeader(header);
    }

    public String generateAccessToken(AuthClaims authClaims) {
        return jwtUtil.createAccessToken(authClaims);
    }

    public String generateRefreshToken(AuthClaims authClaims) {
        return jwtUtil.createRefreshToken(authClaims);
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "None");
        refreshTokenCookie.setMaxAge((int) (jwtProperties.getRefreshTokenExpiration() / 1000));

        response.addCookie(refreshTokenCookie);
    }

    public boolean isTokenValid(String token) {
        if (token == null) {
            return false;
        }
        try {
            return !jwtUtil.isTokenExpired(token) && !jwtUtil.isTokenManipulated(token);
        } catch (JwtException e) {
            return false;
        }
    }


    public AuthClaims extractClaims(String token) {
        return jwtUtil.extractClaimFromToken(token);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "None");
        refreshTokenCookie.setMaxAge(0); // 쿠키 즉시 만료

        response.addCookie(refreshTokenCookie);
    }
}


