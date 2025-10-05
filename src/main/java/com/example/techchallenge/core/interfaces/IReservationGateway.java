package com.example.techchallenge.core.interfaces;

import com.example.techchallenge.core.domain.entities.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IReservationGateway {
    List<Reservation> findAll();
    Reservation createReservation(Reservation reservation);
    List<Reservation> findByRestaurantId(Long restaurantId);
    Optional<Reservation> findById(Long id);
    List<Reservation> findByRestaurantIdAndReservationTime(Long restaurantId, LocalDateTime reservationTime);
    Reservation updateReservationStatus(Reservation reservation);
}