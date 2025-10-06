package com.example.techchallenge.adapters.presenters;

import com.example.techchallenge.core.domain.entities.ItemOrder;
import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.dto.ItemOrderDTO;
import com.example.techchallenge.core.dto.OrderDTO;
import com.example.techchallenge.core.dto.OrderResponse;

import java.util.List;
import java.util.stream.Collectors;


public class OrderPresenter {

    public static OrderDTO toDTO(Order order, ItemOrder itemOrder) {
        List<ItemOrderDTO> itemOrderDTOs = order.getItemOrders().stream()
                .map(item -> new ItemOrderDTO(
                        item.getItemId(),
                        item.getQuantity()
                ))
                .toList();

        return new OrderDTO(
            order.getId(),
            order.getRestaurantId(),
            order.getUserId(),
            order.getDeliveryOrder(),
            order.getTotalPrice(),
            order.getOrderDate(),
            order.getLastModifiedDate(),
            itemOrderDTOs
        );
    }

    public static Order fromDTO(OrderDTO dto) {
        List<ItemOrder> itemOrders = dto.itemOrderDTO().stream()
                .map(itemDTO -> new ItemOrder(
                        itemDTO.itemId(),
                        itemDTO.quantity()
                ))
                .collect(Collectors.toList());

        return new Order(
            dto.id(),
            dto.restaurantId(),
            dto.userId(),
            dto.deliveryOrder(),
            dto.totalPrice(),
            dto.orderDate(),
            dto.lastModifiedDate(),
            itemOrders
        );
    }

    public static OrderResponse ToResponse(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getRestaurantId(),
            order.getUserId(),
            order.getTotalPrice(),
            order.getOrderDate(),
            order.getLastModifiedDate(),
            order.getStatus()
        );
    }
}