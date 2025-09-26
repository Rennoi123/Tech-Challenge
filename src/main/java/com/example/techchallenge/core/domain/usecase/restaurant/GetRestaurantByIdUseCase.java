package com.example.techchallenge.core.domain.usecase.restaurant;

import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import jakarta.persistence.EntityNotFoundException;

public class GetRestaurantByIdUseCase {

    private static final String RESTAURANT_NOT_FOUND_MESSAGE_BY_ID = "Restaurante não encontrado pelo id: ";

    private final IRestaurantGateway restaurantGateway;

    public GetRestaurantByIdUseCase(IRestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(Long id) {
        return restaurantGateway.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + id));
    }
}
