package com.example.techchallenge.adapters.controllers;

import com.example.techchallenge.adapters.presenters.ReservationPresenter;
import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.domain.usecase.reservation.CreateReservationUseCase;
import com.example.techchallenge.core.domain.usecase.reservation.ListReservationsByRestaurantOwnerUseCase;
import com.example.techchallenge.core.domain.usecase.reservation.UpdateReservationStatusUseCase;
import com.example.techchallenge.core.dto.ReservationDTO;
import com.example.techchallenge.core.dto.ReservationResponse;

import com.example.techchallenge.core.dto.UpdateReservationStatusDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final CreateReservationUseCase createReservationUseCase;
    private final UpdateReservationStatusUseCase updateReservationStatusUseCase;
    private final ListReservationsByRestaurantOwnerUseCase listReservationsByRestaurantOwnerUseCase;



    public ReservationController(CreateReservationUseCase createReservationUseCase,
                                 UpdateReservationStatusUseCase updateReservationStatusUseCase,
                                 ListReservationsByRestaurantOwnerUseCase listReservationsByRestaurantOwnerUseCase) {
        this.createReservationUseCase = createReservationUseCase;
        this.updateReservationStatusUseCase = updateReservationStatusUseCase;
        this.listReservationsByRestaurantOwnerUseCase = listReservationsByRestaurantOwnerUseCase;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid ReservationDTO dto) {
        Reservation response = createReservationUseCase.execute(ReservationPresenter.fromDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReservationPresenter.toResponse(response));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> listAll() {
        List<Reservation> reservations = listReservationsByRestaurantOwnerUseCase.execute();
        List<ReservationResponse> response = reservations.stream()
                .map(ReservationPresenter::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<ReservationResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateReservationStatusDTO dto) {
        Reservation updatedReservation = updateReservationStatusUseCase.execute(id, dto.status());
        return ResponseEntity.ok(ReservationPresenter.toResponse(updatedReservation));
    }
}