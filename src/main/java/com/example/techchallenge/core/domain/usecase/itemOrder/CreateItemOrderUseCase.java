package com.example.techchallenge.core.domain.usecase.itemOrder;

import com.example.techchallenge.core.domain.entities.*;
import com.example.techchallenge.core.interfaces.IItemGateway;
import com.example.techchallenge.core.interfaces.IItemOrderGateway;
import com.example.techchallenge.core.interfaces.IOrderGateway;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public class CreateItemOrderUseCase {

    private static final String ITEM_NOT_FOUND_MESSAGE_BY_ID = "Item não encontrado pelo id: ";
    private static final String ORDER_NOT_FOUND_MESSAGE_BY_ID = "Pedido não encontrado pelo id: ";
    private static final String SAVE_ITEM_ORDER_ERROR_MESSAGE = "Falha ao salvar Item do pedido.";

    private final IItemOrderGateway itemOrderGateway;
    private final IItemGateway itemGateway;
    private final IOrderGateway orderGateway;

    private Item item;
    private Order order;

    public CreateItemOrderUseCase(
            IItemOrderGateway itemOrderGateway, IOrderGateway orderGateway, IItemGateway itemGateway) {
        this.orderGateway = orderGateway;
        this.itemGateway = itemGateway;
        this.itemOrderGateway = itemOrderGateway;
    }

    public ItemOrder execute(ItemOrder itemOrder, Restaurant restaurant, User user) {
        validate(itemOrder);
        itemOrder.setPrice(item.getPrice());

        Optional<ItemOrder> saved = Optional.of(itemOrderGateway.addItemToOrder(itemOrder, item, order, restaurant, user))
                .orElseThrow(() -> new EntityNotFoundException(SAVE_ITEM_ORDER_ERROR_MESSAGE));

        return saved.get();
    }

    private void validate(ItemOrder itemOrder) {
        item = itemGateway.findById(itemOrder.getItemId())
                .orElseThrow(() -> new EntityNotFoundException(ITEM_NOT_FOUND_MESSAGE_BY_ID + itemOrder.getItemId()));

        order = orderGateway.getOrderById(itemOrder.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND_MESSAGE_BY_ID + itemOrder.getOrderId()));
    }
}
