package com.example.techchallenge.core.domain.usecase.order;

import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.interfaces.IOrderGateway;

import java.util.List;

public class ListOrdersUseCase {

    private final IOrderGateway orderGatewayAdapter;

    public ListOrdersUseCase(IOrderGateway orderGateway) {
        this.orderGatewayAdapter = orderGateway;
    }

    public List<Order> execute(String status) {
        return orderGatewayAdapter.listOrdersByStatus(status);
    }
}
