package com.example.techchallenge.unit.service;

import com.example.techchallenge.domain.entities.AddressEntity;
import com.example.techchallenge.domain.entities.RestaurantEntity;
import com.example.techchallenge.domain.entities.UserEntity;
import com.example.techchallenge.domain.usecases.RestaurantUseCase;
import com.example.techchallenge.enums.UserRoles;
import com.example.techchallenge.exception.UserNotFoundException;
import com.example.techchallenge.interfaces.IRestaurantGateway;
import com.example.techchallenge.interfaces.IUserGateway;
import com.example.techchallenge.dto.RestaurantRequest;
import com.example.techchallenge.dto.AddressDTO;
import com.example.techchallenge.domain.usecases.AddressUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantGateway restaurantGateway;
    @Mock
    private IUserGateway userGateway;
    @Mock
    private AddressUseCase addressUseCase;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantRequest restaurantRequest;
    private RestaurantEntity restaurantEntity;
    private UserEntity ownerUserEntity;
    private AddressDTO addressDTO;
    private AddressEntity addressEntity;

    @BeforeEach
    void setUp() {
        addressDTO = new AddressDTO(
                1L, "Rua do Restaurante", "456", "Loja B", "Centro", "Sao Paulo", "SP", "01000-000"
        );
        addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Rua do Restaurante");
        addressEntity.setNumber("456");
        addressEntity.setComplement("Loja B");
        addressEntity.setNeighborhood("Centro");
        addressEntity.setCity("Sao Paulo");
        addressEntity.setState("SP");
        addressEntity.setPostalCode("01000-000");

        ownerUserEntity = new UserEntity();
        ownerUserEntity.setId(1L);
        ownerUserEntity.setRoles(UserRoles.ADMIN);
        ownerUserEntity.setName("Owner Name");
        ownerUserEntity.setEmail("owner@example.com");

        restaurantRequest = new RestaurantRequest(
                "Restaurante Teste", addressDTO, "Culinaria Teste",
                LocalTime.of(9, 0), LocalTime.of(22, 0)
        );

        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Restaurante Teste");
        restaurantEntity.setAddress(addressEntity);
        restaurantEntity.setCuisineType("Culinaria Teste");
        restaurantEntity.setOpeningTime(LocalTime.of(9, 0));
        restaurantEntity.setClosingTime(LocalTime.of(22, 0));
        restaurantEntity.setOwner(ownerUserEntity);
    }

    @Test
    @DisplayName("Deve criar restaurante com sucesso")
    void deveCriarRestauranteComSucesso() {
        when(userGateway.findByEmail(anyString())).thenReturn(Optional.of(ownerUserEntity));
        when(restaurantGateway.countByAddressId(anyLong())).thenReturn(0);
        when(addressUseCase.createOrUpdateAddress(any(AddressDTO.class))).thenReturn(addressEntity);
        when(restaurantGateway.createRestaurant(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        RestaurantEntity result = restaurantUseCase.createRestaurant(restaurantRequest);

        assertNotNull(result);
        assertEquals(restaurantRequest.name(), result.getName());
        verify(userGateway, times(1)).findByEmail(anyString());
        verify(restaurantGateway, times(1)).countByAddressId(addressDTO.id());
        verify(addressUseCase, times(1)).createOrUpdateAddress(restaurantRequest.address());
        verify(restaurantGateway, times(1)).createRestaurant(any(RestaurantEntity.class));
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando dono não for encontrado")
    void deveLancarUserNotFoundExceptionQuandoDonoNaoEncontrado() {
        when(userGateway.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            restaurantUseCase.createRestaurant(restaurantRequest);
        });

        verify(restaurantGateway, never()).createRestaurant(any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando dono não tiver papel ADMIN")
    void deveLancarIllegalArgumentExceptionQuandoDonoSemPapelAdmin() {
        ownerUserEntity.setRoles(UserRoles.CLIENTE);
        when(userGateway.findByEmail(anyString())).thenReturn(Optional.of(ownerUserEntity));
        when(restaurantGateway.countByAddressId(anyLong())).thenReturn(0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantUseCase.createRestaurant(restaurantRequest);
        });

        assertEquals("Usuário sem permissão para criar um Restaurante ", exception.getMessage());
        verify(restaurantGateway, never()).createRestaurant(any());
    }

    @Test
    @DisplayName("Deve retornar todos os restaurantes com sucesso")
    void deveRetornarTodosOsRestaurantesComSucesso() {
        when(restaurantGateway.findAll()).thenReturn(Collections.singletonList(restaurantEntity));

        List<RestaurantEntity> results = restaurantUseCase.getAllRestaurants();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(restaurantGateway, times(1)).findAll();
    }

    // ... outros testes do RestaurantUseCase
}