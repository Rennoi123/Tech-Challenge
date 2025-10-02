package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.usecase.item.FindItemsByRestaurantUseCase;
import com.example.techchallenge.core.interfaces.IItemGateway;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindItemsByRestaurantUseCaseTest {

    @Mock
    private IItemGateway itemGateway;

    @InjectMocks
    private FindItemsByRestaurantUseCase findItemsByRestaurantUseCase;

    @Test
    void deveRetornarItensPorRestaurante() {
        Long restaurantId = 1L;

        Item item = new Item(10L, "Pizza", "Massa fina", BigDecimal.valueOf(30),
                false, "/img/pizza.png", restaurantId);

        when(itemGateway.existsById(restaurantId)).thenReturn(true);
        when(itemGateway.findByRestaurantId(restaurantId)).thenReturn(List.of(item));

        List<Item> result = findItemsByRestaurantUseCase.execute(restaurantId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getName());

        verify(itemGateway, times(1)).existsById(restaurantId);
        verify(itemGateway, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoEncontrar() {
        Long restaurantId = 2L;

        when(itemGateway.existsById(restaurantId)).thenReturn(true);
        when(itemGateway.findByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());
        List<Item> result = findItemsByRestaurantUseCase.execute(restaurantId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(itemGateway, times(1)).existsById(restaurantId);
        verify(itemGateway, times(1)).findByRestaurantId(restaurantId);
        verifyNoMoreInteractions(itemGateway);
    }

    @Test
    void deveFalharQuandoRestauranteNaoExistir() {
        Long restaurantId = 3L;
        when(itemGateway.existsById(restaurantId)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> findItemsByRestaurantUseCase.execute(restaurantId));
        verify(itemGateway, times(1)).existsById(restaurantId);
        verify(itemGateway, never()).findByRestaurantId(anyLong());
    }
}
