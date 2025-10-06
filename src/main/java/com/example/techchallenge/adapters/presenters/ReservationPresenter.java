package com.example.techchallenge.adapters.presenters;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.dto.ReservationDTO;
import com.example.techchallenge.core.dto.ReservationResponse;

import java.time.LocalDateTime;

public class ReservationPresenter {

    public static ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRestaurantId(),
                reservation.getUserId(),
                reservation.getReservationTime(),
                reservation.getNumberOfPeople(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }


    public static Reservation fromDTO(ReservationDTO dto) {
        return new Reservation(
                dto.restaurantId(),
                dto.reservationTime(),
                dto.numberOfPeople(),
                LocalDateTime.now()
        );
    }
}