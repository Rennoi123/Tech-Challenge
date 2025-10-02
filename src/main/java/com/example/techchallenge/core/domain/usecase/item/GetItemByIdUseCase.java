package com.example.techchallenge.core.domain.usecase.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.interfaces.IItemGateway;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public class GetItemByIdUseCase {

    private final String ITEM_NOT_FOUND_MESSAGE_BY_ID = "Item não encontrado pelo id: ";

    private final IItemGateway gateway;

    public GetItemByIdUseCase(IItemGateway gateway) {
        this.gateway = gateway;
    }

    public Optional<Item> execute(Long id) {
        return Optional.ofNullable(gateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ITEM_NOT_FOUND_MESSAGE_BY_ID + id)));
    }
}
