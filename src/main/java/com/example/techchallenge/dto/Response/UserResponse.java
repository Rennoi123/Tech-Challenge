package com.example.techchallenge.dto.Response;

public record UserResponse(
        Long id,
        String name,
        String email,
        AddressResponse address
) {}
