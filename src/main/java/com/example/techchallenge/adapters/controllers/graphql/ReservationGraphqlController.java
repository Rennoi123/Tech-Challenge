package com.example.techchallenge.adapters.controllers.graphql;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.domain.usecase.reservation.CreateReservationUseCase;
import com.example.techchallenge.core.domain.usecase.reservation.ListReservationsByRestaurantOwnerUseCase;
import com.example.techchallenge.core.domain.usecase.reservation.UpdateReservationStatusUseCase;
import com.example.techchallenge.core.enums.ReservationStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReservationGraphqlController {

    private final ListReservationsByRestaurantOwnerUseCase listByOwner;
    private final CreateReservationUseCase createReservation;
    private final UpdateReservationStatusUseCase updateReservationStatus;

    public ReservationGraphqlController(ListReservationsByRestaurantOwnerUseCase listByOwner,
                                        CreateReservationUseCase createReservation,
                                        UpdateReservationStatusUseCase updateReservationStatus) {
        this.listByOwner = listByOwner;
        this.createReservation = createReservation;
        this.updateReservationStatus = updateReservationStatus;
    }


    @QueryMapping
    public List<Reservation> reservationsByOwner() {
        return listByOwner.execute();
    }

    @MutationMapping
    public Reservation createReservation(@Argument ReservationInput input) {
        Reservation reservation = new Reservation();
        reservation.setId(input.id);
        reservation.setRestaurantId(input.restaurantId());
        reservation.setUserId(input.userId());
        reservation.setReservationTime(input.reservationTime());
        reservation.setNumberOfPeople(input.numberOfPeople());
        reservation.setStatus(input.status);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(input.updatedAt());
        return createReservation.execute(reservation);
    }

    @MutationMapping
    public Reservation updateReservationStatus(@Argument ReservationStatusInput reservationStatusInput) {

        return updateReservationStatus.execute(reservationStatusInput.id, reservationStatusInput.status());
    }

    public record ReservationInput(Long id,
                                   Long restaurantId,
                                   Long userId,
                                   @JsonFormat(pattern="dd/MM/yyyy HH:mm")
                                   LocalDateTime reservationTime,
                                   Integer numberOfPeople,
                                   ReservationStatusEnum status,
                                   LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {}

    public record ReservationStatusInput(
            Long id,
            ReservationStatusEnum status
    ){}



    @GraphQlExceptionHandler
    public GraphQLError handle(Exception ex) {
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage()).build();
    }
}
