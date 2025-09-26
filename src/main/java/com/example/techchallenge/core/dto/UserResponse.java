package com.example.techchallenge.core.dto;

import com.example.techchallenge.core.enums.UserRoles;

public record UserResponse(
        Long id,
        String name,
        String email,
        AddressResponse address,
        UserRoles roles
) {}
