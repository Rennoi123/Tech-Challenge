package com.example.techchallenge.core.interfaces;

import com.example.techchallenge.core.domain.entities.*;

import java.util.Optional;

public interface IItemOrderGateway {
    Optional<ItemOrder> addItemToOrder(ItemOrder itemOrder, Item item, Order order, Restaurant restaurant, User user);
}
