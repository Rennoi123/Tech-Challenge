package com.example.techchallenge.service;

import com.example.techchallenge.dto.Request.AddressRequest;
import com.example.techchallenge.dto.Request.RestaurantRequest;
import com.example.techchallenge.dto.Response.AddressResponse;
import com.example.techchallenge.dto.Response.RestaurantResponse;
import com.example.techchallenge.entities.AddressEntity;
import com.example.techchallenge.entities.RestaurantEntity;
import com.example.techchallenge.entities.UserEntity;
import com.example.techchallenge.enums.UserRoles;
import com.example.techchallenge.exception.UserNotFoundException;
import com.example.techchallenge.repository.RestaurantRepository;
import com.example.techchallenge.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressService addressService;

    @InjectMocks
    private RestaurantService restaurantService;

    private RestaurantRequest restaurantRequest;
    private RestaurantEntity restaurantEntity;
    private UserEntity ownerUserEntity;
    private AddressRequest addressRequest;
    private AddressEntity addressEntity;
    private AddressResponse addressResponse;

    @BeforeEach
    void setUp() {
        addressRequest = new AddressRequest(
                null, "Rua do Restaurante", "456", "Loja B", "Centro", "Sao Paulo", "SP", "01000-000"
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

        addressResponse = new AddressResponse(
                1L, "Rua do Restaurante", "456", "Loja B", "Centro", "Sao Paulo", "SP", "01000-000"
        );

        ownerUserEntity = new UserEntity();
        ownerUserEntity.setId(1L);
        ownerUserEntity.setRoles(UserRoles.RESTAURANTE);
        ownerUserEntity.setName("Owner Name");
        ownerUserEntity.setEmail("owner@example.com");

        restaurantRequest = new RestaurantRequest(
                "Restaurante Teste", addressRequest, "Culinaria Teste",
                LocalTime.of(9, 0), LocalTime.of(22, 0), 1L
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
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(ownerUserEntity));
        when(addressService.createOrUpdateAddress(any(AddressRequest.class))).thenReturn(addressEntity);
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        RestaurantResponse response = restaurantService.createRestaurant(restaurantRequest);

        assertNotNull(response);
        assertEquals(restaurantRequest.name(), response.name());
        assertEquals(restaurantRequest.cuisineType(), response.cuisineType());
        assertEquals(restaurantRequest.ownerId(), response.ownerId());
        assertEquals(restaurantRequest.openingTime(), response.openingTime());
        assertEquals(restaurantRequest.closingTime(), response.closingTime());
        assertEquals(addressResponse, response.address());

        verify(userRepository, times(1)).findById(restaurantRequest.ownerId());
        verify(addressService, times(1)).createOrUpdateAddress(restaurantRequest.address());
        verify(restaurantRepository, times(1)).save(any(RestaurantEntity.class));
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando dono não for encontrado")
    void deveLancarUserNotFoundExceptionQuandoDonoNaoEncontrado() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            restaurantService.createRestaurant(restaurantRequest);
        });

        assertEquals("Dono do restaurante não encontrado pelo ID: " + restaurantRequest.ownerId(), exception.getMessage());
        verify(userRepository, times(1)).findById(restaurantRequest.ownerId());
        verify(addressService, never()).createOrUpdateAddress(any());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando dono não tiver papel RESTAURANTE")
    void deveLancarIllegalArgumentExceptionQuandoDonoSemPapelRestaurante() {
        ownerUserEntity.setRoles(UserRoles.CLIENTE);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(ownerUserEntity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantService.createRestaurant(restaurantRequest);
        });

        assertEquals("Usuário  sem permissão para criar um Restaurante ", exception.getMessage());
        verify(userRepository, times(1)).findById(restaurantRequest.ownerId());
        verify(addressService, never()).createOrUpdateAddress(any());
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar todos os restaurantes com sucesso")
    void deveRetornarTodosOsRestaurantesComSucesso() {
        when(restaurantRepository.findAll()).thenReturn(Collections.singletonList(restaurantEntity));

        List<RestaurantResponse> responses = restaurantService.getAllRestaurants();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(restaurantEntity.getName(), responses.get(0).name());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar restaurante por ID com sucesso")
    void deveRetornarRestaurantePorIdComSucesso() {
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurantEntity));

        RestaurantResponse response = restaurantService.getRestaurantById(1L);

        assertNotNull(response);
        assertEquals(restaurantEntity.getId(), response.id());
        assertEquals(restaurantEntity.getName(), response.name());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando restaurante não encontrado pelo ID")
    void deveLancarEntityNotFoundExceptionQuandoRestauranteNaoEncontradoPorId() {
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            restaurantService.getRestaurantById(99L);
        });

        assertEquals("Restaurante não encontrado pelo id: 99", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve deletar restaurante pelo ID com sucesso")
    void deveDeletarRestaurantePorIdComSucesso() {
        when(restaurantRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(restaurantRepository).deleteById(anyLong());

        restaurantService.deleteRestaurant(1L);

        verify(restaurantRepository, times(1)).existsById(1L);
        verify(restaurantRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar deletar restaurante inexistente")
    void deveLancarEntityNotFoundExceptionAoDeletarRestauranteInexistente() {
        when(restaurantRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            restaurantService.deleteRestaurant(99L);
        });

        assertEquals("Restaurante não encontrado pelo id: 99", exception.getMessage());
        verify(restaurantRepository, times(1)).existsById(99L);
        verify(restaurantRepository, never()).deleteById(anyLong());
    }
}
