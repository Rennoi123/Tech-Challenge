package com.example.techchallenge.core.domain.usecase.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.interfaces.IItemGateway;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class FindItemsByRestaurantUseCase {

    private static final String RESTAURANT_NOT_FOUND_MESSAGE_BY_ID = "Restaurante não encontrado pelo id: ";

    private final IItemGateway restaurantGateway;

    public FindItemsByRestaurantUseCase(IItemGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public List<Item> execute(Long restaurantId) {
        if (!restaurantGateway.existsById(restaurantId)) {
            throw new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + restaurantId);
        }

        return restaurantGateway.findByRestaurantId(restaurantId);
    }
}
