package com.example.techchallenge.core.dto;

import com.example.techchallenge.core.enums.ReservationStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReservationResponse (
        Long id,
        Long restaurantId,
        Long userId,
        @JsonFormat(pattern="dd/MM/yyyy HH:mm")
        LocalDateTime reservationTime,
        Integer numberOfPeople,
        @Enumerated(EnumType.STRING)
        ReservationStatusEnum status,
        @JsonFormat(pattern="dd/MM/yyyy HH:mm")
        LocalDateTime createdAt,
        @JsonFormat(pattern="dd/MM/yyyy HH:mm")
        LocalDateTime updateAt
){}
