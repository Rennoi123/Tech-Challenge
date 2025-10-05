package com.example.techchallenge.adapters.gateways;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.interfaces.IReservationGateway;
import com.example.techchallenge.infrastructure.entities.ReservationEntity;
import com.example.techchallenge.infrastructure.repository.ReservationRepository;
import com.example.techchallenge.infrastructure.repository.RestaurantRepository;
import com.example.techchallenge.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReservationGatewayAdapter implements IReservationGateway {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public ReservationGatewayAdapter(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        var restaurantEntity = restaurantRepository.findById(reservation.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
        var userEntity = userRepository.findById(reservation.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        var reservationEntity = ReservationEntity.fromDomain(reservation, restaurantEntity, userEntity);
        var savedEntity = reservationRepository.save(reservationEntity);
        return savedEntity.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByRestaurantId(Long restaurantId) {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getRestaurant().getId().equals(restaurantId))
                .map(ReservationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id).map(ReservationEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByRestaurantIdAndReservationTime(Long restaurantId, LocalDateTime reservationTime) {
        return reservationRepository.findByRestaurantIdAndReservationTime(restaurantId, reservationTime)
                .stream()
                .map(ReservationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Reservation updateReservationStatus(Reservation reservation) {
        var reservationEntity = reservationRepository.findById(reservation.getId())
                .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada com id: " + reservation.getId()));

        reservationEntity.setStatus(reservation.getStatus());
        reservationEntity.setUpdatedAt(LocalDateTime.now());

        var savedEntity = reservationRepository.save(reservationEntity);
        return savedEntity.toDomain();
    }
}