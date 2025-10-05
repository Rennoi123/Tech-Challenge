package com.example.techchallenge.core.domain.usecase.restaurant;

import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import jakarta.persistence.EntityNotFoundException;

public class UpdateRestaurantUseCase {

    private final String RESTAURANT_NOT_FOUND = "Restaurante não encontrado: ";
;

    private final IRestaurantGateway gateway;

    public UpdateRestaurantUseCase(IRestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public Restaurant execute(Restaurant restaurant) {
        restaurant.validate();

        gateway.findById(restaurant.getId())
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND + restaurant.getId()));

        return gateway.save(restaurant);
    }
}
