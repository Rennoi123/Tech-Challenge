package com.example.techchallenge.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record RestaurantDTO(
        @NotBlank(message = "Nome: é obrigatório")
        String name,

        @NotNull(message = "Endereço: é obrigatório")
        AddressDTO address,

        @NotBlank(message = "Tipo de cozinha: é obrigatório")
        String cuisineType,

        @NotNull(message = "Horário de funcionamento: é obrigatório")
        LocalTime openingTime,

        @NotNull(message = "Horário de funcionamento: é obrigatório")
        LocalTime closingTime,

        @NotNull(message = "Quantidade de mesa: é obrigatório")
        Integer qtdTable

) {}