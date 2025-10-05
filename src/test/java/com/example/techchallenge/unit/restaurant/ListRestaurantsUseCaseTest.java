package com.example.techchallenge.unit.restaurant;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.domain.usecase.restaurant.ListRestaurantsUseCase;
import com.example.techchallenge.core.enums.UserRoles;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ListRestaurantsUseCaseTest {

    @Mock
    private IRestaurantGateway gateway;

    @InjectMocks
    private ListRestaurantsUseCase useCase;

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address(
                1L, "Rua A", "123", "Ap 1", "Centro", "Cidade X", "SP", "12345-000"
        );
    }

    @Test
    void deveListarRestaurantes() {
        Restaurant restaurant = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10,0), LocalTime.of(22,0), 1L);

        when(gateway.findAll()).thenReturn(List.of(restaurant));

        List<Restaurant> result = useCase.execute();

        assertFalse(result.isEmpty());
        assertEquals("Restaurante A", result.get(0).getName());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistir() {
        when(gateway.findAll()).thenReturn(Collections.emptyList());

        List<Restaurant> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
