package com.example.techchallenge.core.interfaces;

import com.example.techchallenge.core.domain.entities.Order;

public interface IMessagePublisher {
    void publishOrderCreated(Order order);
    void publishOrderCompleted(Order order);
}
