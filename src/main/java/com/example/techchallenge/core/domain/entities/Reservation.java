package com.example.techchallenge.core.domain.entities;

import com.example.techchallenge.core.enums.ReservationStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reservation {

    private static final String RESTAURANT_ID_REQUIRED = "Restaurante é obrigatório";
    private static final String USER_ID_REQUIRED = "Usuário é obrigatório";
    private static final String RESERVATION_TIME_REQUIRED = "A data e hora da reserva são obrigatórias";
    private static final String RESERVATION_TIME_IN_FUTURE = "A data da reserva deve ser no futuro";
    private static final String NUMBER_OF_PEOPLE_REQUIRED = "O número de pessoas é obrigatório";
    private static final String NUMBER_OF_PEOPLE_MIN = "O número de pessoas deve ser no mínimo 1";

    private Long id;
    private Long restaurantId;
    private Long userId;
    private LocalDateTime reservationTime;
    private Integer numberOfPeople;
    private ReservationStatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Reservation(Long id, Long restaurantId, Long userId, LocalDateTime reservationTime, Integer numberOfPeople, ReservationStatusEnum status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.reservationTime = reservationTime;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Reservation(Long restaurantId, LocalDateTime reservationTime, Integer numberOfPeople, LocalDateTime createdAt) {
        this.restaurantId = restaurantId;
        this.reservationTime = reservationTime;
        this.numberOfPeople = numberOfPeople;
        this.createdAt = createdAt;
    }

    public void validate() {
        validateField(restaurantId, RESTAURANT_ID_REQUIRED);
        validateField(userId, USER_ID_REQUIRED);
        validateField(reservationTime, RESERVATION_TIME_REQUIRED);
        validateField(numberOfPeople, NUMBER_OF_PEOPLE_REQUIRED);

        if (reservationTime == null || reservationTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(RESERVATION_TIME_IN_FUTURE);
        }

        if (numberOfPeople == null ||  numberOfPeople <= 0) {
            throw new IllegalArgumentException(NUMBER_OF_PEOPLE_MIN);
        }
    }

    private void validateField(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}