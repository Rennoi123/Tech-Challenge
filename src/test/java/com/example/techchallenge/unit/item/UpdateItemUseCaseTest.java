package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.usecase.item.UpdateItemUseCase;
import com.example.techchallenge.core.dto.ItemDTO;
import com.example.techchallenge.core.dto.ItemResponse;
import com.example.techchallenge.core.interfaces.IItemGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateItemUseCaseTest {

    @Mock
    private IItemGateway itemGateway;

    @Mock
    private IRestaurantGateway restaurantGateway;

    @InjectMocks
    private UpdateItemUseCase updateItemUseCase;

    @Test
    void deveAtualizarItemComSucesso() {
        Address address = new Address(1L, "Rua A", "123", "Ap 1",
                "Centro", "Cidade X", "SP", "12345-000");
        Item existing = new Item(1L, "Pizza", "Massa fina", BigDecimal.valueOf(30),
                false, "/img/pizza.png", 1L);
        ItemDTO dto = new ItemDTO("Pizza Atualizada", "Massa grossa", BigDecimal.valueOf(35),
                true, "/img/pizza2.png", 1L);
        Restaurant restaurant = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(9,0), LocalTime.of(23,0), 1L, 50, 10);

        when(itemGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));
        when(itemGateway.save(any())).thenReturn(new Item(1L, dto.name(), dto.description(), dto.price(), dto.dineInOnly(), dto.photoPath(), 1L));

        ItemResponse result = updateItemUseCase.execute(1L, dto);

        assertNotNull(result);
        assertEquals("Pizza Atualizada", result.name());
    }

    @Test
    void deveFalharQuandoItemNaoExiste() {
        ItemDTO dto = new ItemDTO("Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 1L);

        when(itemGateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> updateItemUseCase.execute(1L, dto));
    }

    @Test
    void deveFalharQuandoRestauranteNaoExiste() {
        Item existing = new Item(1L, "Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 1L);
        ItemDTO dto = new ItemDTO("Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 99L);

        when(itemGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> updateItemUseCase.execute(1L, dto));
    }
}
