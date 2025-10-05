package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.usecase.item.DeleteItemUseCase;
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
class DeleteItemUseCaseTest {

    @Mock
    private IItemGateway itemGateway;

    @InjectMocks
    private DeleteItemUseCase deleteItemUseCase;

    @Test
    void deveDeletarItemComSucesso() {
        when(itemGateway.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> deleteItemUseCase.execute(1L));

        verify(itemGateway, times(1)).existsById(1L);
        verify(itemGateway, times(1)).deleteById(1L);
    }

    @Test
    void deveFalharQuandoItemNaoExiste() {
        when(itemGateway.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> deleteItemUseCase.execute(1L));

        verify(itemGateway, times(1)).existsById(1L);
        verify(itemGateway, never()).deleteById(anyLong());
    }
}
