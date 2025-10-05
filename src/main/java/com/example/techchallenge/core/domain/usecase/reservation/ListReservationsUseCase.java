package com.example.techchallenge.core.domain.usecase.reservation;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.interfaces.IReservationGateway;

import java.util.List;

public class ListReservationsUseCase {

    private final IReservationGateway reservationGateway;

    public ListReservationsUseCase(IReservationGateway reservationGateway) {
        this.reservationGateway = reservationGateway;
    }

    public List<Reservation> execute() {
        return reservationGateway.findAll();
    }
}