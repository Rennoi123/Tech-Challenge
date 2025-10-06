package com.example.techchallenge.core.domain.usecase.order;

import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.interfaces.IOrderGateway;
import jakarta.persistence.EntityNotFoundException;

public class GetOrderByIdUseCase {

    private final String ORDER_NOT_FOUND_MESSAGE_BY_ID = "Order não encontrado pelo id: ";

    private final IOrderGateway gateway;

    public GetOrderByIdUseCase(IOrderGateway gateway) {
        this.gateway = gateway;
    }

    public Order execute(Long id) {
        return gateway.getOrderById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND_MESSAGE_BY_ID + id));
    }
}
