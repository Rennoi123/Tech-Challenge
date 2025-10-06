package com.example.techchallenge.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderDTO (
        Long id,

        @NotNull @NotBlank(message = "ID do restaurante é obrigatório")
        Long restaurantId,

        @NotNull @NotBlank(message = "ID do usuário é obrigatório")
        Long userId,

        @NotNull @NotBlank(message = "Pedido para entrega é obrigatório")
        Boolean deliveryOrder,

        BigDecimal totalPrice,
        String orderDate,
        String lastModifiedDate,

        @JsonProperty("ItemOrderDTO")
        @NotEmpty(message = "O pedido deve conter pelo menos um item")
        List<@Valid ItemOrderDTO> itemOrderDTO
){}
