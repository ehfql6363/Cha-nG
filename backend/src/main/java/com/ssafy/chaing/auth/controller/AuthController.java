package com.ssafy.chaing.auth.controller;

import com.ssafy.chaing.auth.controller.request.FcmRequest;
import com.ssafy.chaing.auth.controller.request.LoginRequest;
import com.ssafy.chaing.auth.controller.request.SignupRequest;
import com.ssafy.chaing.auth.controller.response.UserInfoResponse;
import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.auth.service.AuthService;
import com.ssafy.chaing.auth.service.command.SignupCommand;
import com.ssafy.chaing.auth.service.dto.AuthDTO;
import com.ssafy.chaing.common.schema.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "사용자가 회원가입을 하고 JWT 토큰과 사용자 정보를 받습니다.")
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<UserInfoResponse>> signup(@Valid @ModelAttribute SignupRequest body,
                                                                 HttpServletResponse response) {
        AuthDTO authDTO = authService.signup(
                new SignupCommand(body.getEmailAddress(), body.getPassword(), body.getName()),
                response
        );

        UserInfoResponse result = UserInfoResponse.from(authDTO.getUserInfo());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authDTO.getAccessToken())
                .body(BaseResponse.success(result));
    }

    @Operation(summary = "로그인", description = "사용자가 로그인을 하고 JWT 토큰과 사용자 정보를 받습니다.")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<UserInfoResponse>> login(@Valid @ModelAttribute LoginRequest body,
                                                                HttpServletResponse response) {
        AuthDTO authDTO = authService.login(body.getEmailAddress(), body.getPassword(), response);
        UserInfoResponse result = UserInfoResponse.from(authDTO.getUserInfo());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authDTO.getAccessToken())
                .body(BaseResponse.success(result));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse<UserInfoResponse>> reissue(HttpServletRequest request,
                                                                  HttpServletResponse response) {
        AuthDTO authDTO = authService.reissueTokens(request, response);
        UserInfoResponse result = UserInfoResponse.from(authDTO.getUserInfo());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authDTO.getAccessToken())
                .body(BaseResponse.success(result));
    }

    @Operation(summary = "fcm 설정", description = "사용자가 fcm토큰을 설정합니다.")
    @PostMapping("/fcm")
    public ResponseEntity<BaseResponse<Void>> registerFcmToken(@Valid @RequestBody FcmRequest request,
                                                               @AuthenticationPrincipal UserPrincipal user) {
        authService.updateFcmToken(request.toCommand(user.getId()));

        return ResponseEntity.ok().body(BaseResponse.success(null));
    }

    @Operation(summary = "로그아웃", description = "사용자가 로그아웃하여 토큰과 fcm을 지웁니다.")
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(
            @AuthenticationPrincipal UserPrincipal user,
            HttpServletResponse response
    ) {
        authService.logout(user.getId(), response);
        return ResponseEntity.ok().body(BaseResponse.success(null));
    }

}
