package com.example.techchallenge.adapters.gateways;

import com.example.techchallenge.core.domain.entities.*;
import com.example.techchallenge.core.interfaces.IItemOrderGateway;
import com.example.techchallenge.infrastructure.entities.*;
import com.example.techchallenge.infrastructure.repository.ItemOrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ItemOrderGatewayAdapter implements IItemOrderGateway {

    private final ItemOrderRepository itemOrderRepository;

    public ItemOrderGatewayAdapter(ItemOrderRepository itemOrderRepository) {
        this.itemOrderRepository = itemOrderRepository;
    }

    @Override
    public Optional<ItemOrder> addItemToOrder(ItemOrder itemOrder, Item item, Order order, Restaurant restaurant, User user) {
        UserEntity userEntity = UserEntity.fromDomain(user);
        RestaurantEntity restaurantEntityv = RestaurantEntity.fromDomain(restaurant);
        ItemEntity itemEntity = ItemEntity.fromDomain(item, restaurantEntityv);
        OrderEntity orderEntity = OrderEntity.fromDomain(order, userEntity, restaurantEntityv);
        ItemOrderEntity itemOrderEntity = ItemOrderEntity.fromDomain(itemOrder, itemEntity, orderEntity);
        return Optional.of(itemOrderRepository.save(itemOrderEntity).toDomain());
    }
}
