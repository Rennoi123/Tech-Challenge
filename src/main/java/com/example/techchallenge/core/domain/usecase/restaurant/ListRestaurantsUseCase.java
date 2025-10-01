package com.example.techchallenge.core.domain.usecase.restaurant;

import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;

import java.util.List;

public class ListRestaurantsUseCase {

    private final IRestaurantGateway gateway;

    public ListRestaurantsUseCase(IRestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public List<Restaurant> execute() {
        return gateway.findAll();
    }
}
