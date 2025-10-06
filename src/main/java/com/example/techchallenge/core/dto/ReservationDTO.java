package com.example.techchallenge.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationDTO (

    @NotNull(message = "O ID do restaurante é obrigatório")
    Long restaurantId,

    @NotNull(message = "A data e hora da reserva são obrigatórias")
    @Future(message = "A data e hora da reserva devem estar no futuro")
    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    LocalDateTime reservationTime,

    @NotNull(message = "O número de pessoas é obrigatório")
    @Min(value = 1, message = "O número de pessoas deve ser no mínimo 1")
    Integer numberOfPeople
    ){}