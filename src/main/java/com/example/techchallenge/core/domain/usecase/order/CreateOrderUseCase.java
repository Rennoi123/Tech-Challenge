package com.example.techchallenge.core.domain.usecase.order;

import com.example.techchallenge.core.domain.entities.ItemOrder;
import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.domain.usecase.itemOrder.CreateItemOrderUseCase;
import com.example.techchallenge.core.interfaces.IMessagePublisher;
import com.example.techchallenge.core.interfaces.IOrderGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.infrastructure.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CreateOrderUseCase {

    private static final String RESTAURANT_NOT_FOUND_MESSAGE_BY_ID = "Restaurante não encontrado pelo id: ";
    private static final String USER_NOT_FOUND_MESSAGE_BY_ID = "Usuário não encontrado pelo id: ";
    private static final String VALID_MESSAGE = "Somente administradores podem criar pedidos.";
    private static final String VALID_MESSAGE_ORDER_MUST_HAVE_AT_LEAST_ONE_ITEM = "Pedido deve possuir ao menos um item.";

    private final IOrderGateway orderGateway;
    private final IRestaurantGateway restaurantGateway;
    private final IUserGateway userGateway;
    private final IMessagePublisher messagePublisher;
    private final CreateItemOrderUseCase createItemOrderUseCase;
    private final SecurityUtils securityUtils;

    private User user;
    private Restaurant restaurant;

    public CreateOrderUseCase(
            IOrderGateway orderGateway,
            IRestaurantGateway restaurantGateway,
            IUserGateway userGateway,
            IMessagePublisher messagePublisher,
            CreateItemOrderUseCase createItemOrderUseCase,
            SecurityUtils securityUtils) {
        this.createItemOrderUseCase = createItemOrderUseCase;
        this.securityUtils = securityUtils;
        this.messagePublisher = messagePublisher;
        this.userGateway = userGateway;
        this.restaurantGateway = restaurantGateway;
        this.orderGateway = orderGateway;
    }

    public Order execute(Order order) {
        validate(order);

        Order orderSaved = orderGateway.createOrder(order, user, restaurant);
        Long orderId = orderSaved.getId();

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ItemOrder itemOrder : order.getItemOrders()) {
            totalPrice = processAndPersistItemOrder(itemOrder, restaurant, user, orderId).add(totalPrice);
        }

        orderSaved.setTotalPrice(totalPrice);
        updateOrderTotal(orderId, totalPrice);

        messagePublisher.publishOrderCreated(orderSaved);

        return orderSaved;
    }

    private void validate(Order order) {
        if (!securityUtils.isAdmin()) {
            throw new IllegalArgumentException(VALID_MESSAGE);
        }

        if (order.getItemOrders() == null || order.getItemOrders().isEmpty()) {
            throw new IllegalArgumentException(VALID_MESSAGE_ORDER_MUST_HAVE_AT_LEAST_ONE_ITEM);
        }

        this.restaurant = restaurantGateway.findById(order.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException(
                        RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + order.getRestaurantId()));

        this.user = userGateway.findById(order.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + order.getUserId()));
    }

    private BigDecimal processAndPersistItemOrder(
                ItemOrder itemOrder, Restaurant restaurant, User user, Long orderId) {
            itemOrder.setOrderId(orderId);
            createItemOrderUseCase.execute(itemOrder, restaurant, user);

            return itemOrder.getPrice()
                    .multiply(BigDecimal.valueOf(itemOrder.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
    }

    private void updateOrderTotal(Long orderId, BigDecimal totalPrice) {
        orderGateway.updateOrderTotal(orderId, totalPrice);
    }
}
