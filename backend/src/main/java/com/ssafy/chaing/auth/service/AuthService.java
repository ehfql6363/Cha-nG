package com.ssafy.chaing.auth.service;

import com.ssafy.chaing.auth.service.command.FcmCommand;
import com.ssafy.chaing.auth.service.command.SignupCommand;
import com.ssafy.chaing.auth.service.dto.AuthDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthDTO signup(SignupCommand command, HttpServletResponse response);

    AuthDTO login(String emailAddress, String password, HttpServletResponse response);

    AuthDTO reissueTokens(HttpServletRequest request, HttpServletResponse response);

    void updateFcmToken(FcmCommand command);

    void logout(Long userId, HttpServletResponse response);
}
