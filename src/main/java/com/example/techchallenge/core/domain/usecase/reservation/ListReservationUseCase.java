package com.example.techchallenge.core.domain.usecase.reservation;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.exception.UserNotFoundException;
import com.example.techchallenge.core.interfaces.IReservationGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;

import java.util.List;

public class ListReservationUseCase {

    private static final String USER_NOT_FOUND_MESSAGE_BY_EMAIL = "Usuário não encontrado pelo ID: ";


    private final IReservationGateway reservationGateway;
    private final IUserGateway userGateway;
    private final ISecurityGateway securityGateway;

    public ListReservationUseCase(
            IReservationGateway reservationGateway,
            IUserGateway userGateway,
            ISecurityGateway securityGateway
    ) {
        this.reservationGateway = reservationGateway;
        this.userGateway = userGateway;
        this.securityGateway = securityGateway;
    }

    public List<Reservation> execute() {
        String email = securityGateway.getAuthenticatedEmail();


        User user = userGateway.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_EMAIL));

        return reservationGateway.findAllByUserId(user.getId());
    }
}