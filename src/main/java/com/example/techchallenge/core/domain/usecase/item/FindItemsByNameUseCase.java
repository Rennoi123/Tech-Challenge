package com.example.techchallenge.core.domain.usecase.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.interfaces.IItemGateway;

import java.util.List;

public class FindItemsByNameUseCase {

    private final IItemGateway gateway;

    public FindItemsByNameUseCase(IItemGateway gateway) {
        this.gateway = gateway;
    }

    public List<Item> execute(String name) {
        return gateway.findByNameContaining(name);
    }
}
