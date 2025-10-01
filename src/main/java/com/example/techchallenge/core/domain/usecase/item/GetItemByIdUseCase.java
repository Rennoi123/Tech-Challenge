package com.example.techchallenge.core.domain.usecase.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.interfaces.IItemGateway;

import java.util.Optional;

public class GetItemByIdUseCase {

    private final IItemGateway gateway;

    public GetItemByIdUseCase(IItemGateway gateway) {
        this.gateway = gateway;
    }

    public Optional<Item> execute(Long id) {
        return gateway.findById(id);
    }
}
