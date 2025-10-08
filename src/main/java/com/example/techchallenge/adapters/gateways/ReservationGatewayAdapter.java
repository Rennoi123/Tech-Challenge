package com.example.techchallenge.adapters.gateways;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.interfaces.IReservationGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.infrastructure.entities.ReservationEntity;
import com.example.techchallenge.infrastructure.repository.ReservationRepository;
import com.example.techchallenge.infrastructure.repository.RestaurantRepository;
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
    private final IUserGateway userGateway;

    public ReservationGatewayAdapter(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository, IUserGateway userGateway,
                                     ISecurityGateway securityGateway) {
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.userGateway = userGateway;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByRestaurantId(Long restaurantId) {
        return reservationRepository.findAllByRestaurantId(restaurantId).stream()
                .map(ReservationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        var restaurantEntity = restaurantRepository.findById(reservation.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

        User userEntity = userGateway.findById(reservation.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        var reservationEntity = ReservationEntity.fromDomain(reservation, restaurantEntity, userEntity);
        var savedEntity = reservationRepository.save(reservationEntity);
        return savedEntity.toDomain();
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

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByRestaurantIdIn(List<Long> restaurantIds) {
        return reservationRepository.findByRestaurantIdIn(restaurantIds).stream()
                .map(ReservationEntity::toDomain)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAllByUserId(Long userId) {
        return reservationRepository.findAllByUserId(userId).stream()
                .map(ReservationEntity::toDomain)
                .collect(Collectors.toList());
    }
}