package com.example.techchallenge.core.domain.usecase.reservation;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.enums.ReservationStatusEnum;
import com.example.techchallenge.core.exception.ReservationNotFoundException;
import com.example.techchallenge.core.interfaces.IReservationGateway;

public class UpdateReservationStatusUseCase {

    private final IReservationGateway reservationGateway;

    public UpdateReservationStatusUseCase(IReservationGateway reservationGateway) {
        this.reservationGateway = reservationGateway;
    }

    public Reservation execute(Long id, ReservationStatusEnum status) {
        Reservation reservation = reservationGateway.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada com id: " + id));

        reservation.setStatus(status);

        return reservationGateway.updateReservationStatus(reservation);
    }
}