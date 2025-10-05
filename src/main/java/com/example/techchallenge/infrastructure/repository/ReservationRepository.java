package com.example.techchallenge.infrastructure.repository;

import com.example.techchallenge.infrastructure.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByRestaurantIdAndReservationTime(Long restaurantId, LocalDateTime reservationTime);

}