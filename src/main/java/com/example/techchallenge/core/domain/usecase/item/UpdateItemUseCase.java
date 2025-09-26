package com.example.techchallenge.core.domain.usecase.item;

import com.example.techchallenge.adapters.presenters.ItemPresenter;
import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.dto.ItemDTO;
import com.example.techchallenge.core.dto.ItemResponse;
import com.example.techchallenge.core.interfaces.IItemGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.domain.entities.Restaurant;
import jakarta.persistence.EntityNotFoundException;


public class UpdateItemUseCase {

    private static final String ITEM_NOT_FOUND_MESSAGE_BY_ID = "Item não encontrado pelo id: ";
    private static final String RESTAURANT_NOT_FOUND_MESSAGE_BY_ID = "Restaurante não encontrado pelo id: ";

    private final IItemGateway itemGateway;
    private final IRestaurantGateway restaurantGateway;

    public UpdateItemUseCase(IItemGateway itemGateway, IRestaurantGateway restaurantGateway) {
        this.itemGateway = itemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public ItemResponse execute(Long id, ItemDTO request) {
        Item existingItem = itemGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ITEM_NOT_FOUND_MESSAGE_BY_ID + id));

        Restaurant restaurant = restaurantGateway.findById(request.restaurantId())
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + request.restaurantId()));

        existingItem.setName(request.name());
        existingItem.setDescription(request.description());
        existingItem.setPrice(request.price());
        existingItem.setDineInOnly(request.dineInOnly());
        existingItem.setPhotoPath(request.photoPath());
        existingItem.setRestaurantId(restaurant.getId());

        existingItem.validate();

        Item updated = itemGateway.save(existingItem);

        return ItemPresenter.toResponse(updated);
    }
}
