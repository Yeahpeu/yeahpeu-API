package com.yeahpeu.user.entity;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN("어드민"),
    USER("유저");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }
}