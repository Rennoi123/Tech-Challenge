package com.example.techchallenge.adapters.presenters;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.dto.ItemDTO;
import com.example.techchallenge.core.dto.ItemResponse;

public class ItemPresenter {

    public static ItemDTO toDTO(Item item) {
        return new ItemDTO(
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.isDineInOnly(),
                item.getPhotoPath(),
                item.getRestaurantId()
        );
    }

    public static ItemResponse toResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.isDineInOnly(),
                item.getPhotoPath(),
                item.getRestaurantId()
        );
    }

    public static Item fromDTO(ItemDTO dto) {
        return new Item(
                null,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.dineInOnly(),
                dto.photoPath(),
                dto.restaurantId()
        );
    }
}
