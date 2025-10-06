package com.example.techchallenge.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestaurantResponse(
        Long id,
        String name,
        String cuisineType,
        LocalTime openingTime,
        LocalTime closingTime,
        AddressDTO address,
        Integer capacity,
        Integer qtdTable
) {}