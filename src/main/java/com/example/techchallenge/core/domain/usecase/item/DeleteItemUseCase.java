package com.example.techchallenge.core.domain.usecase.item;

import com.example.techchallenge.core.interfaces.IItemGateway;
import jakarta.persistence.EntityNotFoundException;


public class DeleteItemUseCase {

    private static final String ITEM_NOT_FOUND_MESSAGE_BY_ID = "Item não encontrado pelo id: ";

    private final IItemGateway itemGateway;

    public DeleteItemUseCase(IItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public void execute(Long id) {
        if (!itemGateway.existsById(id)) {
            throw new EntityNotFoundException(ITEM_NOT_FOUND_MESSAGE_BY_ID + id);
        }

        itemGateway.deleteById(id);
    }
}
