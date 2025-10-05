package com.example.techchallenge.infrastructure.entities;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.enums.ReservationStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "RESERVATIONS")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @NotNull
    @Future
    @Column(nullable = false)
    private LocalDateTime reservationTime;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer numberOfPeople;

    @Enumerated(EnumType.STRING)
    private ReservationStatusEnum status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ReservationEntity fromDomain(Reservation reservation, RestaurantEntity restaurantEntity, UserEntity userEntity) {
        ReservationEntity entity = new ReservationEntity();
        entity.id = reservation.getId();
        entity.restaurant = restaurantEntity;
        entity.user = userEntity;
        entity.reservationTime = reservation.getReservationTime();
        entity.numberOfPeople = reservation.getNumberOfPeople();
        entity.status = reservation.getStatus();
        entity.createdAt = reservation.getCreatedAt();
        entity.updatedAt = reservation.getUpdatedAt();
        return entity;
    }

    public Reservation toDomain() {
        return new Reservation(
                this.id,
                this.restaurant != null ? this.restaurant.getId() : null,
                this.user != null ? this.user.getId() : null,
                this.reservationTime,
                this.numberOfPeople,
                this.status,
                this.createdAt,
                this.updatedAt
        );
    }

}