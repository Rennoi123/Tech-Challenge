package com.example.techchallenge.dto.Response;

import com.example.techchallenge.enums.UserRoles;

public record UserResponse(
        Long id,
        String name,
        String email,
        AddressResponse address,
        UserRoles roles
) {}
