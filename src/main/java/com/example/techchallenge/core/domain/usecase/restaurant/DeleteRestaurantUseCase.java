package com.example.techchallenge.core.domain.usecase.restaurant;

import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import jakarta.persistence.EntityNotFoundException;

public class DeleteRestaurantUseCase {

    private static final String RESTAURANT_NOT_FOUND_MESSAGE_BY_ID = "Restaurante não encontrado pelo id: ";

    private final IRestaurantGateway restaurantGateway;

    public DeleteRestaurantUseCase(IRestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public void execute(Long id) {
        if (restaurantGateway.findById(id).isEmpty()) {
            throw new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + id);
        }
        restaurantGateway.deleteById(id);
    }
}
