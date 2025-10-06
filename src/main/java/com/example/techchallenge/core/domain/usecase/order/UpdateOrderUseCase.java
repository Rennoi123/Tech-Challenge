package com.example.techchallenge.core.domain.usecase.order;

import com.example.techchallenge.adapters.gateways.UserGatewayAdapter;
import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.enums.UserRoles;
import com.example.techchallenge.core.interfaces.IMessagePublisher;
import com.example.techchallenge.core.interfaces.IOrderGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.infrastructure.security.SecurityUtils;

public class UpdateOrderUseCase {

    private static final String VALID_MESSAGE = "Somente administradores podem atualizar pedidos.";
    private static final String ORDER_NOT_FOUND_MESSAGE_BY_ID = "Pedido não encontrado pelo id: ";

    private final IOrderGateway orderGateway;
    private final IMessagePublisher messagePublisher;
    private final SecurityUtils securityUtils;

    public UpdateOrderUseCase(
            IOrderGateway orderGateway,
            IMessagePublisher messagePublisher,
            SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
        this.messagePublisher = messagePublisher;
        this.orderGateway = orderGateway;
    }

    public Order execute(Long orderId, String status) {
        if (!securityUtils.isAdmin()) {
            throw new IllegalArgumentException(VALID_MESSAGE);
        }

        Order order = orderGateway.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND_MESSAGE_BY_ID + orderId));

        Order updatedOrder = orderGateway.updateOrderStatus(orderId, status);

        if ("FINALIZADO".equalsIgnoreCase(status)) {
            messagePublisher.publishOrderCompleted(updatedOrder);
        }

        return updatedOrder;
    }
}
