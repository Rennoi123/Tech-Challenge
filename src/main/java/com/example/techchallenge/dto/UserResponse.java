package com.example.techchallenge.dto;

public record UserResponse(
        Long id,
        String name,
        String email,
        AddressResponse address
) {}
