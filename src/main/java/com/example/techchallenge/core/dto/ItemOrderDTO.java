package com.example.techchallenge.core.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ItemOrderDTO(

        @NotNull @NotBlank(message = "ID do item é obrigatório")
        Long itemId,

        @NotNull @NotBlank(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "Quantidade deve ser no mínimo 1")
        Integer quantity
) {}
