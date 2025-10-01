package com.example.techchallenge.core.domain.usecase.item;

import com.example.techchallenge.adapters.presenters.ItemPresenter;
import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.dto.ItemDTO;
import com.example.techchallenge.core.dto.ItemResponse;
import com.example.techchallenge.core.interfaces.IItemGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import jakarta.persistence.EntityNotFoundException;

public class CreateItemUseCase {

    private static final String RESTAURANT_NOT_FOUND_MESSAGE_BY_ID = "Restaurante n?o encontrado pelo id: ";

    private final IItemGateway itemGateway;
    private final IRestaurantGateway restaurantGateway;

    public CreateItemUseCase(IItemGateway itemGateway, IRestaurantGateway restaurantGateway) {
        this.itemGateway = itemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public ItemResponse execute(ItemDTO request) {
        Restaurant restaurant = restaurantGateway.findById(request.restaurantId())
                .orElseThrow(() -> new EntityNotFoundException(
                        RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + request.restaurantId()));

        Item item = new Item(
                null,
                request.name(),
                request.description(),
                request.price(),
                request.dineInOnly(),
                request.photoPath(),
                restaurant.getId()
        );

        item.validate();

        Item saved = itemGateway.save(item);

        return ItemPresenter.toResponse(saved);
    }
}
