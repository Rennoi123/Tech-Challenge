package com.example.techchallenge.core.domain.usecase.restaurant;

import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;

public class UpdateRestaurantUseCase {

    private final IRestaurantGateway gateway;

    public UpdateRestaurantUseCase(IRestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public Restaurant execute(Restaurant restaurant) {
        restaurant.validate();
        return gateway.save(restaurant);
    }
}
