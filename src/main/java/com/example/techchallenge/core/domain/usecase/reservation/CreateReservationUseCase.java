package com.example.techchallenge.core.domain.usecase.reservation;

import com.example.techchallenge.core.domain.entities.Reservation;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.enums.ReservationStatusEnum;
import com.example.techchallenge.core.exception.UserNotFoundException;
import com.example.techchallenge.core.interfaces.IReservationGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CreateReservationUseCase {
    private static final String USER_NOT_FOUND_MESSAGE_BY_EMAIL = "Usuário não encontrado pelo e-mail: ";

    private static final String USER_NOT_FOUND_MESSAGE_BY_ID = "Usuário não encontrado pelo ID: ";
    private static final String RESTAURANTE_NOT_FOUND_MESSAGE_BY_ID = "Restaurante não encontrado pelo ID: ";
    private static final String INSUFFICIENT_TABLES = "Total de mesas insuficiente pois o total é: ";
    private static final String  NUMBER_OF_POPLE_RESERVATION_EXCEEDS = "O número de pessoas da reserva excede a capacidade total do restaurante.";

    private final IReservationGateway reservationGateway;
    private final IRestaurantGateway restaurantGateway;
    private final IUserGateway userGateway;
    private final ISecurityGateway securityGateway;

    public CreateReservationUseCase(
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

    public Reservation execute(Reservation reservation) {
        String email = securityGateway.getAuthenticatedEmail();
        User user = userGateway.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_EMAIL + email));

        reservation.setUserId(user.getId());

        reservation.validate();
        reservation.setStatus(ReservationStatusEnum.PENDENTE);

        Restaurant restaurant = findRestaurantOrFail(reservation.getRestaurantId());

        findUserOrFail(user.getId());

        validateBusinessRules(reservation, restaurant);

        return reservationGateway.createReservation(reservation);
    }

    private void validateBusinessRules(Reservation newReservation, Restaurant restaurant) {

        if (newReservation.getNumberOfPeople() > restaurant.getCapacity()) {
            throw new IllegalArgumentException(NUMBER_OF_POPLE_RESERVATION_EXCEEDS);
        }

        LocalDateTime dateTime = newReservation.getReservationTime();
        List<Reservation> existingReservations = reservationGateway.findByRestaurantIdAndReservationTime(newReservation.getRestaurantId(), dateTime);

        int peopleInExistingReservations = existingReservations.stream()
                .filter(r -> r.getStatus() == ReservationStatusEnum.APROVADA
                        || r.getStatus() == ReservationStatusEnum.PENDENTE)
                .mapToInt(Reservation::getNumberOfPeople)
                .sum();


        int availableCapacity = restaurant.getCapacity() - peopleInExistingReservations;

        if (newReservation.getNumberOfPeople() > availableCapacity) {
            throw new IllegalArgumentException(INSUFFICIENT_TABLES + availableCapacity );
        }
    }

    private Restaurant findRestaurantOrFail(Long restaurantId) {
        return restaurantGateway.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(RESTAURANTE_NOT_FOUND_MESSAGE_BY_ID + restaurantId));
    }

    private void findUserOrFail(Long userId) {
        Optional<User> user = userGateway.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException(USER_NOT_FOUND_MESSAGE_BY_ID + userId  );
        }
    }
}