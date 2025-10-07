package com.example.techchallenge.unit.restaurant;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.domain.usecase.restaurant.UpdateRestaurantUseCase;
import com.example.techchallenge.core.enums.UserRoles;
import com.example.techchallenge.core.exception.UserNotFoundException;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.infrastructure.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
class UpdateRestaurantUseCaseTest {

    @Mock
    private IRestaurantGateway gateway;

    @Mock
    private ISecurityGateway securityGateway;

    @Mock
    private IUserGateway userGateway;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UpdateRestaurantUseCase useCase;

    private Address address;
    private User user;

    @BeforeEach
    void setUp() {
        address = new Address(
                1L, "Rua A", "123", "Ap 1", "Centro", "Cidade X", "SP", "12345-000"
        );

        user = new User(1L, "João", "joao@email.com", "123456", UserRoles.ADMIN, address);
    }

    @Test
    void deveAtualizarRestauranteComSucesso() {
        Restaurant existing = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10,0), LocalTime.of(22,0), 1L, 100, 200);

        Restaurant updated = new Restaurant(1L, "Restaurante B", address, "Japonesa",
                LocalTime.of(9,0), LocalTime.of(23,0), 1L, 100, 200);

        when(securityUtils.isAdmin()).thenReturn(true);
        when(gateway.findById(1L)).thenReturn(Optional.of(existing));
        when(securityGateway.getAuthenticatedEmail()).thenReturn("joao@email.com");
        when(userGateway.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(gateway.save(any(Restaurant.class), any(User.class))).thenReturn(updated);

        Restaurant result = useCase.execute(updated);

        assertNotNull(result);
        assertEquals("Restaurante B", result.getName());
        assertEquals("Japonesa", result.getCuisineType());

        verify(gateway, times(1)).save(any(Restaurant.class), any(User.class));
    }

    @Test
    void deveFalharSeNaoForAdmin() {
        Restaurant restaurant = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10,0), LocalTime.of(22,0), 1L, 100, 200);

        when(securityUtils.isAdmin()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(restaurant));
        verify(gateway, never()).save(any(), any());
    }

    @Test
    void deveFalharSeRestauranteNaoExistir() {
        Restaurant restaurant = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10,0), LocalTime.of(22,0), 1L, 100, 200);

        when(securityUtils.isAdmin()).thenReturn(true);
        when(gateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurant));
    }

    @Test
    void deveFalharSeUsuarioNaoForEncontrado() {
        Restaurant restaurant = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10,0), LocalTime.of(22,0), 1L, 100, 200);

        when(securityUtils.isAdmin()).thenReturn(true);
        when(gateway.findById(1L)).thenReturn(Optional.of(restaurant));
        when(securityGateway.getAuthenticatedEmail()).thenReturn("joao@email.com");
        when(userGateway.findByEmail("joao@email.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(restaurant));
    }
}
