package com.example.techchallenge.core.interfaces;

import com.example.techchallenge.core.domain.entities.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IReservationGateway {
    Reservation createReservation(Reservation reservation);
    Optional<Reservation> findById(Long id);
    List<Reservation> findByRestaurantIdAndReservationTime(Long restaurantId, LocalDateTime reservationTime);
    Reservation updateReservationStatus(Reservation reservation);
    List<Reservation> findByRestaurantId(Long restaurantId);
    List<Reservation> findByRestaurantIdIn(List<Long> restaurantIds);
}