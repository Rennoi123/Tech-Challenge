package com.example.techchallenge.core.domain.usecase.reservation;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.enums.ReservationStatusEnum;
import com.example.techchallenge.core.exception.ReservationNotFoundException;
import com.example.techchallenge.core.exception.RestaurantNotFoundException;
import com.example.techchallenge.core.exception.UserNotFoundException;
import com.example.techchallenge.core.interfaces.IReservationGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;

public class UpdateReservationStatusUseCase {
    private static final String USER_NOT_FOUND_MESSAGE_BY_EMAIL = "Usuário não encontrado pelo e-mail: ";
    private static final String RESERVATION_NOT_FOUND_MESSAGE = "Reserva não encontrada com id: ";
    private static final String RESTAURANT_NOT_FOUND_MESSAGE = "Restaurante não encontrado.";
    private static final String UNAUTHORIZED_MESSAGE = "Você não tem permissão para aprovar reservas para este restaurante.";

    private final IReservationGateway reservationGateway;
    private final IRestaurantGateway restaurantGateway;
    private final ISecurityGateway securityGateway;
    private final IUserGateway userGateway;


    public UpdateReservationStatusUseCase(IReservationGateway reservationGateway, IRestaurantGateway restaurantGateway,
                                          ISecurityGateway securityGateway,
                                          IUserGateway userGateway) {
        this.reservationGateway = reservationGateway;
        this.restaurantGateway = restaurantGateway;
        this.securityGateway = securityGateway;
        this.userGateway = userGateway;
    }

    public Reservation execute(Long id, ReservationStatusEnum status) {
        String email  = securityGateway.getAuthenticatedEmail();

        User user = userGateway.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_EMAIL + email));

        Reservation reservation = reservationGateway.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(RESERVATION_NOT_FOUND_MESSAGE + id));

        Restaurant restaurant = restaurantGateway.findById(reservation.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE));


        if (!user.getId().equals(restaurant.getOwnerId())) {
            throw new IllegalArgumentException(UNAUTHORIZED_MESSAGE);
        }

        reservation.setStatus(status);

        return reservationGateway.updateReservationStatus(reservation);
    }
}