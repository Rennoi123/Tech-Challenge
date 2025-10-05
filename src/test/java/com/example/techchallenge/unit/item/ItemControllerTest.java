package com.example.techchallenge.unit.item;

import com.example.techchallenge.adapters.controllers.ItemController;
import com.example.techchallenge.core.domain.usecase.item.CreateItemUseCase;
import com.example.techchallenge.core.dto.ItemDTO;
import com.example.techchallenge.core.dto.ItemResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private CreateItemUseCase createItemUseCase;

    @InjectMocks
    private ItemController itemController;

    @Test
    void deveCriarItem() {
        ItemDTO dto = new ItemDTO("Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 1L);
        ItemResponse response = new ItemResponse(1L, "Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 1L);

        when(createItemUseCase.execute(dto)).thenReturn(response);

        ResponseEntity<ItemResponse> result = itemController.create(dto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Pizza", result.getBody().name());
    }

    @Test
    void deveFalharAoCriarItem() {
        ItemDTO dto = new ItemDTO("Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 1L);

        when(createItemUseCase.execute(dto)).thenThrow(new RuntimeException("Erro ao criar item"));

        assertThrows(RuntimeException.class, () -> itemController.create(dto));
    }
}
