package com.example.techchallenge.unit.restaurant;

import com.example.techchallenge.adapters.controllers.RestaurantController;
import com.example.techchallenge.adapters.gateways.RestaurantGatewayAdapter;
import com.example.techchallenge.adapters.presenters.AddressPresenter;
import com.example.techchallenge.adapters.presenters.UserPresenter;
import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.domain.usecase.restaurant.CreateRestaurantUseCase;
import com.example.techchallenge.core.dto.AddressDTO;
import com.example.techchallenge.core.dto.RestaurantDTO;
import com.example.techchallenge.core.dto.RestaurantResponse;
import com.example.techchallenge.core.enums.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private CreateRestaurantUseCase createRestaurantUseCase;

    @InjectMocks
    private RestaurantController restaurantController;

    private AddressDTO addressDTO;
    private Address address;
    private User user;

    @BeforeEach
    void setUp() {
        address = new Address(1L, "Rua A", "123", "Ap 1",
                "Centro", "Cidade X", "SP", "12345-000");

        addressDTO = AddressPresenter.toDTO(address);

        user = new User(1L, "João", "joao@email.com", "123456", UserRoles.ADMIN, address);
    }

    @Test
    void deveCriarRestauranteComSucesso() {
        Restaurant domain = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10, 0), LocalTime.of(22, 0), user.getId());
        RestaurantDTO dto = new RestaurantDTO("Restaurante A", addressDTO, "Italiana",
                LocalTime.of(10, 0), LocalTime.of(22, 0));

        when(createRestaurantUseCase.execute(any(Restaurant.class))).thenReturn(domain);

        ResponseEntity<RestaurantResponse> result = restaurantController.create(dto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Restaurante A", result.getBody().name());
        verify(createRestaurantUseCase, times(1)).execute(any(Restaurant.class));
    }

    @Test
    void deveFalharAoCriarRestaurante() {
        RestaurantDTO dto = new RestaurantDTO("Restaurante A", addressDTO, "Italiana",
                LocalTime.of(10, 0), LocalTime.of(22, 0));

        Restaurant domain = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10, 0), LocalTime.of(22, 0), user.getId());

        when(createRestaurantUseCase.execute(domain)).thenThrow(new RuntimeException("Erro"));

        assertThrows(RuntimeException.class, () -> restaurantController.create(dto));
    }
}
