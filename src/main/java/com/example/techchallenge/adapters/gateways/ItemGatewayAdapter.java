package com.example.techchallenge.adapters.gateways;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.interfaces.IItemGateway;
import com.example.techchallenge.infrastructure.entities.ItemEntity;
import com.example.techchallenge.infrastructure.entities.RestaurantEntity;
import com.example.techchallenge.infrastructure.repository.ItemRepository;
import com.example.techchallenge.infrastructure.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ItemGatewayAdapter implements IItemGateway {

    private static final String RESTAURANT_NOT_FOUND_MSG = "Restaurante não encontrado pelo id: ";

    private final ItemRepository itemRepository;
    private final RestaurantRepository restaurantRepository;

    public ItemGatewayAdapter(ItemRepository itemRepository,
                              RestaurantRepository restaurantRepository) {
        this.itemRepository = itemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Item save(Item item) {
        RestaurantEntity restaurant = restaurantRepository.findById(item.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException(RESTAURANT_NOT_FOUND_MSG + item.getRestaurantId()));

        ItemEntity entity = ItemEntity.fromDomain(item, restaurant);
        ItemEntity savedEntity = itemRepository.save(entity);

        return savedEntity.toDomain();
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id).map(ItemEntity::toDomain);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll().stream().map(ItemEntity::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return itemRepository.existsById(id);
    }

    @Override
    public List<Item> findByRestaurantId(Long restaurantId) {
        return itemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(ItemEntity::toDomain)
                .toList();
    }

    @Override
    public List<Item> findByNameContaining(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(ItemEntity::toDomain)
                .toList();
    }

    @Override
    public List<Item> findAvailableItems() {
        return itemRepository.findByAvailableTrue()
                .stream()
                .map(ItemEntity::toDomain)
                .toList();
    }
}
