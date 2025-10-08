package com.example.techchallenge.infrastructure.repository;

import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.infrastructure.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByRestaurantIdAndReservationTime(Long restaurantId, LocalDateTime reservationTime);
    List<ReservationEntity> findAllByRestaurantId(Long restaurantId);
    List<ReservationEntity> findAllByUserId(Long userId);

    List<ReservationEntity> findByRestaurantIdIn(List<Long> restaurantIds);

}