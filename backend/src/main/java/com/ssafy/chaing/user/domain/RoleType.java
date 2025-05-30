package com.ssafy.chaing.user.domain;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN("관리자"),
    USER("유저");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }
}
