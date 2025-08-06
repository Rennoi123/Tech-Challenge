package com.example.techchallenge.dto.Response;

import java.math.BigDecimal;

public record ItemResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean dineInOnly,
        String photoPath,
        Long restaurantId
) {}