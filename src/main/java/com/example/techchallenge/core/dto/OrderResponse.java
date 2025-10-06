package com.example.techchallenge.core.dto;

import java.math.BigDecimal;

public record OrderResponse(
    Long id,
    Long restaurantId,
    Long userId,
    BigDecimal totalPrice,
    String orderDate,
    String lastModifiedDate,
    String status
){}
