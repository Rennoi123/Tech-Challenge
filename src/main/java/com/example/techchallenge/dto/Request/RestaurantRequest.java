package com.example.techchallenge.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record RestaurantRequest(
        @NotBlank(message = "Nome: é obrigatório")
        String name,

        @NotNull(message = "Endereço: é obrigatório")
        AddressRequest address,

        @NotBlank(message = "Tipo de cozinha: é obrigatório")
        String cuisineType,

        @NotNull(message = "Horário de funcionamento: é obrigatório")
        LocalTime openingTime,

        @NotNull(message = "Horário de funcionamento: é obrigatório")
        LocalTime closingTime

) {}