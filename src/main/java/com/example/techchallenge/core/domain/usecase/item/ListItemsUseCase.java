package com.example.techchallenge.core.domain.usecase.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.interfaces.IItemGateway;

import java.util.List;

public class ListItemsUseCase {

    private final IItemGateway gateway;

    public ListItemsUseCase(IItemGateway gateway) {
        this.gateway = gateway;
    }

    public List<Item> execute() {
        return gateway.findAll();
    }
}
