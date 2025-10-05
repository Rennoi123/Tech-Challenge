package com.example.techchallenge.core.dto;

import com.example.techchallenge.core.enums.ReservationStatusEnum;
import jakarta.validation.constraints.NotNull;

public record UpdateReservationStatusDTO (
        @NotNull(message = "O status é obrigatório")
        ReservationStatusEnum status

){}