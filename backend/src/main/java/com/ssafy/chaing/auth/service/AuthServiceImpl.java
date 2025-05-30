package com.ssafy.chaing.auth.service;

import com.ssafy.chaing.auth.jwt.AuthClaims;
import com.ssafy.chaing.auth.jwt.JwtService;
import com.ssafy.chaing.auth.service.command.FcmCommand;
import com.ssafy.chaing.auth.service.command.SignupCommand;
import com.ssafy.chaing.auth.service.dto.AuthDTO;
import com.ssafy.chaing.common.exception.AuthenticationException;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.common.exception.NotFoundException;
import com.ssafy.chaing.group.domain.GroupEntity;
import com.ssafy.chaing.group.repository.GroupRepository;
import com.ssafy.chaing.user.domain.RoleType;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import com.ssafy.chaing.user.service.dto.UserDetailInfoDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public AuthDTO signup(SignupCommand command, HttpServletResponse response) {
        userRepository.findByEmailAddress(command.getEmailAddress()).ifPresent(user -> {
            throw new BadRequestException(ExceptionCode.DUPLICATE_EMAIL);
        });

        // 새로운 사용자 생성
        UserEntity user = UserEntity.builder()
                .emailAddress(command.getEmailAddress())
                .password(passwordEncoder.encode(command.getPassword()))
                .name(command.getName())
                .roleType(RoleType.USER)
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(new AuthClaims(user.getId()));
        String refreshToken = jwtService.generateRefreshToken(new AuthClaims(user.getId()));

        jwtService.setRefreshTokenCookie(response, refreshToken);

        UserDetailInfoDTO dto = new UserDetailInfoDTO(
                user.getId(), user.getName(), user.getNickname(),
                null, null, null);

        return new AuthDTO(accessToken, dto);
    }

    @Override
    public AuthDTO login(String emailAddress, String password, HttpServletResponse response) {
        UserEntity user = userRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException(ExceptionCode.INVALID_PASSWORD);
        }

        String accessToken = jwtService.generateAccessToken(new AuthClaims(user.getId()));
        String refreshToken = jwtService.generateRefreshToken(new AuthClaims(user.getId()));

        jwtService.setRefreshTokenCookie(response, refreshToken);

        Long contractId = null;
        if (user.getGroupId() != null) {
            contractId = groupRepository.findById(user.getGroupId())
                    .map(GroupEntity::getContractId)
                    .orElse(null);
        }

        UserDetailInfoDTO dto = new UserDetailInfoDTO(user.getId(), user.getName(), user.getNickname(),
                user.getProfileImage(), user.getGroupId(), contractId);

        return new AuthDTO(accessToken, dto);
    }

    @Override
    public AuthDTO reissueTokens(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = jwtService.getRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            throw new AuthenticationException(ExceptionCode.INVALID_TOKEN);
        }

        AuthClaims claims = jwtService.extractClaims(refreshToken);

        UserEntity user = userRepository.findById(claims.getUserId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_NOT_FOUND));

        String accessToken = jwtService.generateAccessToken(new AuthClaims(user.getId()));
        String newRefreshToken = jwtService.generateRefreshToken(new AuthClaims(user.getId()));

        jwtService.setRefreshTokenCookie(response, newRefreshToken);

        Long contractId = null;
        if (user.getGroupId() != null) {
            contractId = groupRepository.findById(user.getGroupId())
                    .map(GroupEntity::getContractId)
                    .orElse(null);
        }

        UserDetailInfoDTO dto = new UserDetailInfoDTO(user.getId(), user.getName(), user.getNickname(),
                user.getProfileImage(), user.getGroupId(), contractId);

        return new AuthDTO(accessToken, dto);
    }

    @Override
    @Transactional
    public void updateFcmToken(FcmCommand command) {
        UserEntity user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_NOT_FOUND));
        user.setFcmToken(command.getFcmToken());
    }

    @Override
    @Transactional
    public void logout(Long userId, HttpServletResponse response) {
        // 쿠키에서 Refresh Token 제거
        jwtService.removeRefreshTokenCookie(response);

        // 유저의 FCM 토큰 제거
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_NOT_FOUND));

        user.setFcmToken(null);
    }


}
