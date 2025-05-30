package com.ssafy.chaing.auth.service;

import com.ssafy.chaing.auth.domain.CustomOAuth2User;
import com.ssafy.chaing.auth.service.dto.GoogleDTO;
import com.ssafy.chaing.auth.service.dto.OAuth2DTO;
import com.ssafy.chaing.common.exception.AuthenticationException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.user.domain.RoleType;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2DTO oAuth2DTO = switch (registrationId) {
            case "google" -> new GoogleDTO(oAuth2User.getAttributes());

            default -> throw new AuthenticationException(ExceptionCode.SOCIAL_NOT_FOUND);
        };

        return handleUser(oAuth2DTO);
    }

    public OAuth2User handleUser(OAuth2DTO oAuth2DTO) {
        String email = oAuth2DTO.getEmail();

        UserEntity user = userRepository.findByEmailAddress(email)
                .map(existingUser -> {
                    existingUser.setProviderId(oAuth2DTO.getProviderId()); // 변경 필요 시 업데이트
                    return existingUser;
                })
                .orElse(UserEntity.builder()
                        .emailAddress(oAuth2DTO.getEmail())
                        .name(oAuth2DTO.getName())
                        .nickname(oAuth2DTO.getName())
                        .provider(oAuth2DTO.getProvider())
                        .providerId(oAuth2DTO.getProviderId())
                        .roleType(RoleType.USER)
                        .build());

        UserEntity saved = userRepository.save(user);
        return new CustomOAuth2User(saved, oAuth2DTO);
    }

}

