package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.usecase.item.GetItemByIdUseCase;
import com.example.techchallenge.core.interfaces.IItemGateway;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetItemByIdUseCaseTest {

    @Mock
    private IItemGateway itemGateway;

    @InjectMocks
    private GetItemByIdUseCase getItemByIdUseCase;

    @Test
    void deveRetornarItemPorId() {
        Item item = new Item(1L, "Pizza", "Massa", BigDecimal.TEN, false, "/img.png", 1L);

        when(itemGateway.findById(1L)).thenReturn(Optional.of(item));

        Optional<Item> result = getItemByIdUseCase.execute(1L);

        assertEquals("Pizza", result.get().getName());
    }

    @Test
    void deveFalharQuandoItemNaoExiste() {
        Long id = 1L;
        when(itemGateway.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> getItemByIdUseCase.execute(id));
        verify(itemGateway, times(1)).findById(id);
    }
}
