package com.example.techchallenge.core.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRoles implements GrantedAuthority {
    CLIENTE("user"),
    ADMIN("admin");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }
    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}