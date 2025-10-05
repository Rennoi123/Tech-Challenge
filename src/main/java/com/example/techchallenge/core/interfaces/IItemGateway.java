package com.example.techchallenge.core.interfaces;

import com.example.techchallenge.core.domain.entities.Item;

import java.util.List;
import java.util.Optional;

public interface IItemGateway {
    Item save(Item item);
    Optional<Item> findById(Long id);
    List<Item> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    List<Item> findByRestaurantId(Long restaurantId);
    List<Item> findByNameContaining(String name);
    List<Item> findAvailableItems();
}
