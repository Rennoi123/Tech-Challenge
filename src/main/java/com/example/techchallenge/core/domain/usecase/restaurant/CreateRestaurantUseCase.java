package com.example.techchallenge.core.domain.usecase.restaurant;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.enums.UserRoles;
import com.example.techchallenge.core.exception.UserNotFoundException;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;

public class CreateRestaurantUseCase {

    private static final String USER_NOT_FOUND_MESSAGE_BY_EMAIL = "Usuário não encontrado pelo e-mail: ";
    private static final String VALID_MESSAGE_ADDRESS_RESTAURANT = "Já existe um restaurante vinculado a este endereço.";
    private static final String VALID_MESSAGE_OWNER_RESTAURANT = "Somente administradores podem cadastrar restaurantes.";

    private final IRestaurantGateway restaurantGateway;
    private final IAddressGateway addressGateway;
    private final IUserGateway userGateway;
    private final ISecurityGateway securityGateway;

    public CreateRestaurantUseCase(IRestaurantGateway restaurantGateway,
                                   IAddressGateway addressGateway,
                                   IUserGateway userGateway,
                                   ISecurityGateway securityGateway) {
        this.restaurantGateway = restaurantGateway;
        this.addressGateway = addressGateway;
        this.userGateway = userGateway;
        this.securityGateway = securityGateway;
    }

    public Restaurant execute(Restaurant restaurant) {
        restaurant.validate();

        String email = securityGateway.getAuthenticatedEmail();
        User user = userGateway.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_EMAIL + email));

        if (restaurantGateway.countByAddressId(restaurant.getAddress().getId()) > 0) {
            throw new IllegalArgumentException(VALID_MESSAGE_ADDRESS_RESTAURANT);
        }

        if (!UserRoles.ADMIN.equals(user.getRole())) {
            throw new IllegalArgumentException(VALID_MESSAGE_OWNER_RESTAURANT);
        }

        Address address = addressGateway.save(restaurant.getAddress());
        Integer capacity = restaurant.getQtdtable() * 2;

        Restaurant newRestaurant = new Restaurant(
                null,
                restaurant.getName(),
                address,
                restaurant.getCuisineType(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime(),
                user.getId(),
                capacity,
                restaurant.getQtdtable()
        );

        return restaurantGateway.save(newRestaurant);
    }
}
