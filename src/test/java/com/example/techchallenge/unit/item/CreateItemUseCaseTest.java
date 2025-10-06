package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.usecase.item.CreateItemUseCase;
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
class CreateItemUseCaseTest {

    @Mock
    private IItemGateway itemGateway;

    @Mock
    private IRestaurantGateway restaurantGateway;

    @InjectMocks
    private CreateItemUseCase createItemUseCase;

    @Test
    void deveCriarItemComSucesso() {
        Address address = new Address(1L, "Rua A", "123", "Ap 1",
                "Centro", "Cidade X", "SP", "12345-000");
        ItemDTO dto = new ItemDTO("Pizza", "Massa fina", BigDecimal.valueOf(30),
                false, "/img/pizza.png", 1L);
        Restaurant restaurant = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(9,0), LocalTime.of(23,0), 1L,100,200);

        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));
        when(itemGateway.save(any())).thenReturn(new Item(1L, "Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 1L));

        ItemResponse result = createItemUseCase.execute(dto);

        assertNotNull(result);
        assertEquals("Pizza", result.name());
    }

    @Test
    void deveFalharQuandoRestauranteNaoExiste() {
        ItemDTO dto = new ItemDTO("Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 99L);

        when(restaurantGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> createItemUseCase.execute(dto));
    }
}
