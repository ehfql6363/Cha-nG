package com.ssafy.chaing.auth.handler;


import com.ssafy.chaing.auth.domain.CustomOAuth2User;
import com.ssafy.chaing.auth.jwt.AuthClaims;
import com.ssafy.chaing.auth.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    public CustomOAuth2SuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Value("${app.frontend.url}")
    private String frontEndUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        AuthClaims authClaims = new AuthClaims(Long.valueOf(oAuth2User.getName()));
        String accessToken = jwtService.generateAccessToken(authClaims);
        String refreshToken = jwtService.generateRefreshToken(authClaims);

        jwtService.setRefreshTokenCookie(response, refreshToken);
        response.setHeader("Authorization", "Bearer " + accessToken);

        response.sendRedirect(frontEndUrl + "/oauth");
    }

}
