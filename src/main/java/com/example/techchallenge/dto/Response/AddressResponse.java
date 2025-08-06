package com.example.techchallenge.dto.Response;

public record AddressResponse(
        Long id,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String postalCode
) {}
