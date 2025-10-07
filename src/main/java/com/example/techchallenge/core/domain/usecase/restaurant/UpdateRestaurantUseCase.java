package com.example.techchallenge.core.domain.usecase.restaurant;

import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.exception.UserNotFoundException;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.infrastructure.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;

public class UpdateRestaurantUseCase {

    private static final String RESTAURANT_NOT_FOUND = "Restaurante não encontrado: ";
    private static final String VALID_MESSAGE_OWNER_RESTAURANT = "Somente administradores podem atualizar restaurantes.";
    private static final String USER_NOT_FOUND_MESSAGE_BY_EMAIL = "Usuário não encontrado pelo e-mail: ";

    private final IRestaurantGateway gateway;
    private final ISecurityGateway securityGateway;
    private final IUserGateway userGateway;
    private final SecurityUtils securityUtils;

    public UpdateRestaurantUseCase(
            IRestaurantGateway gateway,
            ISecurityGateway securityGateway,
            IUserGateway userGateway,
            SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
        this.userGateway = userGateway;
        this.securityGateway = securityGateway;
        this.gateway = gateway;
    }

    public Restaurant execute(Restaurant restaurant) {
        validate(restaurant);
        User user = getUser();
        return gateway.save(restaurant, user);
    }

    private void validate(Restaurant restaurant) {
        restaurant.validate();

        if (!securityUtils.isAdmin()) {
            throw new IllegalArgumentException(VALID_MESSAGE_OWNER_RESTAURANT);
        }

        gateway.findById(restaurant.getId())
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND + restaurant.getId()));
    }

    private User getUser() {
        String email = securityGateway.getAuthenticatedEmail();
        return userGateway.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_EMAIL + email));
    }
}
