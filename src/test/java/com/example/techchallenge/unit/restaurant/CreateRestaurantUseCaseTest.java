package com.example.techchallenge.unit.restaurant;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.domain.usecase.restaurant.CreateRestaurantUseCase;
import com.example.techchallenge.core.enums.UserRoles;
import com.example.techchallenge.core.exception.UserNotFoundException;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private IRestaurantGateway restaurantGateway;

    @Mock
    private IUserGateway userGateway;

    @Mock
    private ISecurityGateway securityGateway;

    @Mock
    private IAddressGateway addressGateway;

    @InjectMocks
    private CreateRestaurantUseCase useCase;

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
    void deveCriarRestauranteComSucesso() {
        Restaurant novo = new Restaurant(null, "Restaurante A", address, "Italiana",
                LocalTime.of(10,0), LocalTime.of(22,0), user.getId(),100,200);

        Restaurant salvo = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10,0), LocalTime.of(22,0), user.getId(),100,200);

        when(securityGateway.getAuthenticatedEmail()).thenReturn(user.getEmail());
        when(userGateway.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(addressGateway.save(any(Address.class))).thenReturn(address);
        when(restaurantGateway.save(any(Restaurant.class), any(User.class))).thenReturn(salvo);

        Restaurant result = useCase.execute(novo);

        assertNotNull(result.getId());
        assertEquals("Restaurante A", result.getName());
        assertEquals("Italiana", result.getCuisineType());
        verify(securityGateway, times(1)).getAuthenticatedEmail();
        verify(userGateway, times(1)).findByEmail(user.getEmail());
        verify(addressGateway, times(1)).save(any(Address.class));
        verify(restaurantGateway, times(1)).save(any(Restaurant.class), any(User.class));
    }

    @Test
    void deveFalharSeGatewayLancarExcecao() {
        Restaurant novo = new Restaurant(null, "Restaurante X", address, "Japonesa",
                LocalTime.of(11,0), LocalTime.of(20,0), null,100,200);

        when(securityGateway.getAuthenticatedEmail()).thenReturn("naoexiste@email.com");
        when(userGateway.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(novo));

        verify(addressGateway, never()).save(any(Address.class));
        verify(restaurantGateway, never()).save(any(Restaurant.class), any(User.class));
    }
}
