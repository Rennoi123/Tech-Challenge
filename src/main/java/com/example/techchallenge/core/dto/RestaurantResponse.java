package com.example.techchallenge.core.dto;

import java.time.LocalTime;

public record RestaurantResponse(
        Long id,
        String name,
        String cuisineType,
        LocalTime openingTime,
        LocalTime closingTime,
        AddressDTO address
) {}