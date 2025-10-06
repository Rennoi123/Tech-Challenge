package com.example.techchallenge.core.interfaces;

import com.example.techchallenge.core.domain.entities.ItemOrder;
import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IOrderGateway {
    Order createOrder(Order order, User user, Restaurant restaurant);
    Optional<Order> getOrderById(Long id);
    Order updateOrderStatus(Long orderId, String status);
    void updateOrderTotal(Long orderId, BigDecimal totalPrice);
    List<Order> listOrdersByStatus(String status);
}
