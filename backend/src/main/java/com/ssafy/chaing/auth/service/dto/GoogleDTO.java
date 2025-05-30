package com.ssafy.chaing.auth.service.dto;

import com.ssafy.chaing.user.domain.RoleType;
import com.ssafy.chaing.user.domain.UserEntity;
import java.util.Map;

public class GoogleDTO implements OAuth2DTO {
    private final Map<String, Object> attribute;

    public GoogleDTO(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    public static UserEntity from(OAuth2DTO oAuth2DTO) {
        return UserEntity.builder()
                .emailAddress(oAuth2DTO.getEmail())
                .nickname(oAuth2DTO.getName())
                .provider(oAuth2DTO.getProvider())
                .providerId(oAuth2DTO.getProviderId())
                .roleType(RoleType.USER)
                .build();

    }
}
