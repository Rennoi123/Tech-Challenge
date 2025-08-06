package com.example.techchallenge.service;

import com.example.techchallenge.dto.Request.ItemRequest;
import com.example.techchallenge.dto.Response.ItemResponse;
import com.example.techchallenge.entities.ItemEntity;
import com.example.techchallenge.entities.RestaurantEntity;
import com.example.techchallenge.repository.ItemRepository;
import com.example.techchallenge.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private static final String RESTAURANT_NOT_FOUND_MESSAGE_BY_ID = "Restaurante não encontrado pelo id: ";
    private static final String ITEM_NOT_FOUND_MESSAGE_BY_ID = "Item não encontrado pelo id: ";

    private final ItemRepository itemRepository;
    private final RestaurantRepository restaurantRepository;

    public ItemService(ItemRepository ItemRepository, RestaurantRepository restaurantRepository) {
        this.itemRepository = ItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        RestaurantEntity restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + request.restaurantId()));

        ItemEntity item = new ItemEntity();
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setDineInOnly(request.dineInOnly());
        item.setPhotoPath(request.photoPath());
        item.setRestaurant(restaurant);

        ItemEntity savedItem = itemRepository.save(item);
        return toResponse(savedItem);
    }

    public ItemResponse updateItem(Long id, ItemRequest request) {
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ITEM_NOT_FOUND_MESSAGE_BY_ID + id));

        RestaurantEntity restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + request.restaurantId()));

        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setDineInOnly(request.dineInOnly());
        item.setPhotoPath(request.photoPath());
        item.setRestaurant(restaurant);

        ItemEntity updatedItem = itemRepository.save(item);

        return toResponse(updatedItem);
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> getItemsByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + restaurantId);
        }
        return itemRepository.findByRestaurantId(restaurantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteItem(Long id) {
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ITEM_NOT_FOUND_MESSAGE_BY_ID + id));

        itemRepository.delete(item);
    }

    private ItemResponse toResponse(ItemEntity entity) {
        return new ItemResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.isDineInOnly(),
                entity.getPhotoPath(),
                entity.getRestaurant().getId()
        );
    }
}