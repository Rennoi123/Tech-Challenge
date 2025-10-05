package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.usecase.item.FindItemsByNameUseCase;
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
class FindItemsByNameUseCaseTest {

    @Mock
    private IItemGateway itemGateway;

    @InjectMocks
    private FindItemsByNameUseCase findItemsByNameUseCase;

    @Test
    void deveRetornarItensPorNome() {
        Item item = new Item(1L, "Pizza Calabresa", "Massa", BigDecimal.TEN, false, "/img.png", 1L);

        when(itemGateway.findByNameContaining("Pizza")).thenReturn(List.of(item));

        List<Item> result = findItemsByNameUseCase.execute("Pizza");

        assertFalse(result.isEmpty());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoEncontrar() {
        when(itemGateway.findByNameContaining("X")).thenReturn(Collections.emptyList());

        List<Item> result = findItemsByNameUseCase.execute("X");

        assertTrue(result.isEmpty());
    }
}
