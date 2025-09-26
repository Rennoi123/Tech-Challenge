package com.example.techchallenge.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ItemDTO(
        @NotBlank(message = "Nome do item: é obrigatório")
        String name,
        String description,
        @NotNull(message = "Preço: é obrigatório")
        @Positive(message = "Preço: deve ser positivo")
        BigDecimal price,
        boolean dineInOnly,
        String photoPath,
        @NotNull
        Long restaurantId
) {}