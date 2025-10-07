package com.example.techchallenge.unit.restaurant;

import com.example.techchallenge.adapters.gateways.RestaurantGatewayAdapter;
import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.enums.UserRoles;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import com.example.techchallenge.infrastructure.entities.AddressEntity;
import com.example.techchallenge.infrastructure.entities.RestaurantEntity;
import com.example.techchallenge.infrastructure.entities.UserEntity;
import com.example.techchallenge.infrastructure.repository.AddressRepository;
import com.example.techchallenge.infrastructure.repository.RestaurantRepository;
import com.example.techchallenge.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantGatewayAdapterTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private IAddressGateway addressGateway;

    @InjectMocks
    private RestaurantGatewayAdapter gatewayAdapter;

    private Address address;
    private User user;

    @BeforeEach
    void setUp() {
        gatewayAdapter = new RestaurantGatewayAdapter(restaurantRepository, addressRepository);

        address = new Address(1L, "Rua A", "123", "Ap 1",
                "Centro", "Cidade X", "SP", "12345-000");

        user = new User(1L, "João", "joao@email.com", "123456", UserRoles.ADMIN, address);
    }

    @Test
    void deveSalvarRestaurante() {
        Restaurant restaurante = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10, 0), LocalTime.of(22, 0), user.getId(),100,200);

        AddressEntity addressEntity = AddressEntity.fromDomain(address);
        UserEntity userEntity = UserEntity.fromDomain(user);
        RestaurantEntity restauranteEntity = RestaurantEntity.fromDomain(restaurante, addressEntity, userEntity);

        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(addressEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restauranteEntity);

        Restaurant salvo = gatewayAdapter.save(restaurante, user);

        assertNotNull(salvo.getId());
        assertEquals("Restaurante A", salvo.getName());
        assertEquals("Italiana", salvo.getCuisineType());

        verify(addressRepository, times(1)).findById(address.getId());
        verify(restaurantRepository, times(1)).save(any(RestaurantEntity.class));
    }

    @Test
    void deveBuscarRestaurantePorId() {
        Restaurant domain = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10, 0), LocalTime.of(22, 0), user.getId(),100,200);
        RestaurantEntity entity = RestaurantEntity.fromDomain(domain);
        entity.setId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Restaurant> result = gatewayAdapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Restaurante A", result.get().getName());
    }

    @Test
    void deveRetornarEmptyQuandoNaoEncontrarRestaurante() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Restaurant> result = gatewayAdapter.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void deveListarRestaurantes() {
        Restaurant domain = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10, 0), LocalTime.of(22, 0), user.getId(),100,200);
        RestaurantEntity entity = RestaurantEntity.fromDomain(domain);
        entity.setId(1L);

        when(restaurantRepository.findAll()).thenReturn(List.of(entity));

        List<Restaurant> result = gatewayAdapter.findAll();

        assertFalse(result.isEmpty());
        assertEquals("Restaurante A", result.get(0).getName());
    }
}
