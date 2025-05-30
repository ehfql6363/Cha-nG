package com.ssafy.chaing.auth.service.dto;

public interface OAuth2DTO {
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
