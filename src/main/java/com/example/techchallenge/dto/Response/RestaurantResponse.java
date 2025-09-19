package com.example.techchallenge.dto.Response;

import java.time.LocalTime;

public record RestaurantResponse(
        Long id,
        String name,
        String cuisineType,
        LocalTime openingTime,
        LocalTime closingTime,
        AddressResponse address
        ) {}