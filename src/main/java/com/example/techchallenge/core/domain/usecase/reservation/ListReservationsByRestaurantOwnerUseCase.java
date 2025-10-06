package com.example.techchallenge.core.domain.usecase.reservation;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.exception.UserNotFoundException;
import com.example.techchallenge.core.interfaces.IReservationGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListReservationsByRestaurantOwnerUseCase {

    private static final String USER_NOT_FOUND_MESSAGE_BY_EMAIL = "Usuário não encontrado pelo e-mail: ";


    private final IReservationGateway reservationGateway;
    private final IRestaurantGateway restaurantGateway;
    private final IUserGateway userGateway;
    private final ISecurityGateway securityGateway;

    public ListReservationsByRestaurantOwnerUseCase(
            IReservationGateway reservationGateway,
            IRestaurantGateway restaurantGateway,
            IUserGateway userGateway,
            ISecurityGateway securityGateway
    ) {
        this.reservationGateway = reservationGateway;
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
        this.securityGateway = securityGateway;
    }

    public List<Reservation> execute() {
        String email = securityGateway.getAuthenticatedEmail();
        User user = userGateway.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_EMAIL));

        List<Restaurant> userRestaurants = restaurantGateway.findByOwnerId(user.getId());

        if (userRestaurants.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> restaurantIds = userRestaurants.stream()
                .map(Restaurant::getId)
                .collect(Collectors.toList());

        return reservationGateway.findByRestaurantIdIn(restaurantIds);
    }
}