package com.example.techchallenge.unit.restaurant;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.usecase.restaurant.DeleteRestaurantUseCase;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeleteRestaurantUseCaseTest {

    @Mock
    private IRestaurantGateway gateway;

    @InjectMocks
    private DeleteRestaurantUseCase useCase;

    @Test
    void deveDeletarRestaurante() {
        Address address = new Address(1L, "Rua A", "123", null,
                "Centro", "Cidade X", "SP", "12345-000");

        Restaurant restaurant = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10, 0), LocalTime.of(22, 0), 1L);

        when(gateway.findById(1L)).thenReturn(Optional.of(restaurant));

        assertDoesNotThrow(() -> useCase.execute(1L));

        verify(gateway, times(1)).deleteById(1L);
    }

    @Test
    void deveFalharQuandoRestauranteNaoExistir() {
        when(gateway.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(1L));
    }
}
