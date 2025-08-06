package com.example.techchallenge.unit.controller;

import com.example.techchallenge.controller.RestaurantController;
import com.example.techchallenge.dto.Request.AddressRequest;
import com.example.techchallenge.dto.Request.RestaurantRequest;
import com.example.techchallenge.dto.Response.AddressResponse;
import com.example.techchallenge.dto.Response.RestaurantResponse;
import com.example.techchallenge.exception.UserNotFoundException;
import com.example.techchallenge.service.RestaurantService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    private RestaurantRequest restaurantRequest;
    private RestaurantResponse restaurantResponse;
    private AddressRequest addressRequest;
    private AddressResponse addressResponse;

    @BeforeEach
    void setUp() {
        addressRequest = new AddressRequest(
            null, "Rua do Restaurante", "456", "Loja B", "Centro", "Sao Paulo", "SP", "01000-000"
        );
        addressResponse = new AddressResponse(
            1L, "Rua do Restaurante", "456", "Loja B", "Centro", "Sao Paulo", "SP", "01000-000"
        );

        restaurantRequest = new RestaurantRequest(
            "Restaurante Teste", addressRequest, "Culinaria Teste",
            LocalTime.of(9, 0), LocalTime.of(22, 0), 1L
        );
        restaurantResponse = new RestaurantResponse(
            1L, "Restaurante Teste", "Culinaria Teste",
            LocalTime.of(9, 0), LocalTime.of(22, 0), 1L, addressResponse
        );
    }

    @Test
    @DisplayName("Deve criar um restaurante com sucesso")
    void deveCriarRestauranteComSucesso() {
        when(restaurantService.createRestaurant(any(RestaurantRequest.class))).thenReturn(restaurantResponse);

        ResponseEntity<RestaurantResponse> response = restaurantController.createRestaurant(restaurantRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(restaurantResponse, response.getBody());
        verify(restaurantService, times(1)).createRestaurant(restaurantRequest);
    }

    @Test
    @DisplayName("Deve retornar todos os restaurantes")
    void deveRetornarTodosOsRestaurantes() {
        List<RestaurantResponse> respostasEsperadas = Collections.singletonList(restaurantResponse);
        when(restaurantService.getAllRestaurants()).thenReturn(respostasEsperadas);

        ResponseEntity<List<RestaurantResponse>> response = restaurantController.getAllRestaurants();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(respostasEsperadas, response.getBody());
        verify(restaurantService, times(1)).getAllRestaurants();
    }

    @Test
    @DisplayName("Deve retornar restaurante por ID")
    void deveRetornarRestaurantePorId() {
        when(restaurantService.getRestaurantById(anyLong())).thenReturn(restaurantResponse);

        ResponseEntity<RestaurantResponse> response = restaurantController.getRestaurantById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurantResponse, response.getBody());
        verify(restaurantService, times(1)).getRestaurantById(1L);
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND ao buscar restaurante inexistente por ID")
    void deveRetornarNotFoundAoBuscarRestauranteInexistentePorId() {
        when(restaurantService.getRestaurantById(anyLong()))
                .thenThrow(new EntityNotFoundException("Restaurante n�o encontrado"));

        EntityNotFoundException excecao = assertThrows(EntityNotFoundException.class, () -> {
            restaurantController.getRestaurantById(99L);
        });

        assertEquals("Restaurante n�o encontrado", excecao.getMessage());
        verify(restaurantService, times(1)).getRestaurantById(99L);
    }

    @Test
    @DisplayName("Deve deletar um restaurante com sucesso")
    void deveDeletarRestauranteComSucesso() {
        doNothing().when(restaurantService).deleteRestaurant(anyLong());

        ResponseEntity<Void> response = restaurantController.deleteRestaurant(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(restaurantService, times(1)).deleteRestaurant(1L);
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND ao deletar restaurante inexistente")
    void deveRetornarNotFoundAoDeletarRestauranteInexistente() {
        doThrow(new EntityNotFoundException("Restaurante n�o encontrado"))
                .when(restaurantService).deleteRestaurant(anyLong());

        EntityNotFoundException excecao = assertThrows(EntityNotFoundException.class, () -> {
            restaurantController.deleteRestaurant(99L);
        });

        assertEquals("Restaurante n�o encontrado", excecao.getMessage());
        verify(restaurantService, times(1)).deleteRestaurant(99L);
    }

    @Test
    @DisplayName("Deve retornar BAD_REQUEST ao criar restaurante com ownerId sem perfil RESTAURANTE")
    void deveRetornarBadRequestAoCriarRestauranteComOwnerIdSemPerfilRestaurante() {
        when(restaurantService.createRestaurant(any(RestaurantRequest.class)))
                .thenThrow(new IllegalArgumentException("Usu�rio  sem permiss�o para criar um Restaurante "));

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            restaurantController.createRestaurant(restaurantRequest);
        });

        assertEquals("Usu�rio  sem permiss�o para criar um Restaurante ", excecao.getMessage());
        verify(restaurantService, times(1)).createRestaurant(restaurantRequest);
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND ao criar restaurante com ownerId inexistente")
    void deveRetornarNotFoundAoCriarRestauranteComOwnerIdInexistente() {
        when(restaurantService.createRestaurant(any(RestaurantRequest.class)))
                .thenThrow(new UserNotFoundException("Dono do restaurante n�o encontrado pelo ID: 99"));

        UserNotFoundException excecao = assertThrows(UserNotFoundException.class, () -> {
            restaurantController.createRestaurant(new RestaurantRequest(
                    "Restaurante Teste", addressRequest, "Culinaria Teste",
                    LocalTime.of(9, 0), LocalTime.of(22, 0), 99L
            ));
        });

        assertEquals("Dono do restaurante n�o encontrado pelo ID: 99", excecao.getMessage());
        verify(restaurantService, times(1)).createRestaurant(any(RestaurantRequest.class));
    }
}