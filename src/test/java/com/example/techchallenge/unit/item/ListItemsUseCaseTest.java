package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.usecase.item.ListItemsUseCase;
import com.example.techchallenge.core.interfaces.IItemGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListItemsUseCaseTest {

    @Mock
    private IItemGateway itemGateway;

    @InjectMocks
    private ListItemsUseCase listItemsUseCase;

    @Test
    void deveListarItens() {
        Item item = new Item(1L, "Pizza", "Massa", BigDecimal.TEN, false, "/img.png", 1L);

        when(itemGateway.findAll()).thenReturn(List.of(item));

        List<Item> result = listItemsUseCase.execute();

        assertFalse(result.isEmpty());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaItens() {
        when(itemGateway.findAll()).thenReturn(Collections.emptyList());

        List<Item> result = listItemsUseCase.execute();

        assertTrue(result.isEmpty());
    }
}
