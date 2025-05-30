package com.ssafy.chaing.auth.domain;

import com.ssafy.chaing.auth.service.dto.OAuth2DTO;
import com.ssafy.chaing.user.domain.UserEntity;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final UserEntity user;
    private final OAuth2DTO oAuth2DTO;

    public CustomOAuth2User(UserEntity user, OAuth2DTO oAuth2DTO) {
        this.user = user;
        this.oAuth2DTO = oAuth2DTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_USER");
    }

    @Override
    public String getName() {
        return String.valueOf(user.getId());
    }

    public String getEmail() {
        return user.getEmailAddress();
    }

    public OAuth2DTO getOAuth2DTO() {
        return oAuth2DTO;
    }
}
