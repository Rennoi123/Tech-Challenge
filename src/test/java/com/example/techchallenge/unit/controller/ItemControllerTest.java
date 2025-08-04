package com.example.techchallenge.unit.controller;

import com.example.techchallenge.controller.ItemController;
import com.example.techchallenge.dto.Request.ItemRequest;
import com.example.techchallenge.dto.Response.ItemResponse;
import com.example.techchallenge.service.ItemService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private ItemRequest itemRequest;
    private ItemResponse itemResponse;

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequest(
            "Hamburguer", "Delicioso hamburguer artesanal", new BigDecimal("35.00"),
            false, "...", 1L
        );
        itemResponse = new ItemResponse(
            1L, "Hamburguer", "Delicioso hamburguer artesanal", new BigDecimal("35.00"),
            false, "...", 1L
        );
    }

    @Test
    @DisplayName("Deve criar um item com sucesso")
    void deveCriarItemComSucesso() {
        when(itemService.createItem(any(ItemRequest.class))).thenReturn(itemResponse);

        ResponseEntity<ItemResponse> response = itemController.createItem(itemRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(itemResponse, response.getBody());
        verify(itemService, times(1)).createItem(itemRequest);
    }

    @Test
    @DisplayName("Deve retornar BAD_REQUEST ao criar item com dados inválidos")
    void deveRetornarBadRequestAoCriarItemComDadosInvalidos() {
        ItemRequest requisicaoInvalida = new ItemRequest(
                "", "Desc", BigDecimal.ONE, true, "path", 1L
        );

        doThrow(new IllegalArgumentException("Nome do item: é obrigatório"))
                .when(itemService).createItem(any(ItemRequest.class));

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            itemController.createItem(requisicaoInvalida);
        });

        assertEquals("Nome do item: é obrigatório", excecao.getMessage());
        verify(itemService, times(1)).createItem(requisicaoInvalida);
    }

    @Test
    @DisplayName("Deve atualizar um item com sucesso")
    void deveAtualizarItemComSucesso() {
        ItemRequest requisicaoAtualizada = new ItemRequest(
                "Cheeseburger", "Queijo e bacon", new BigDecimal("38.00"),
                false, "/images/cheeseburger.jpg", 1L
        );
        ItemResponse respostaAtualizada = new ItemResponse(
                1L, "Cheeseburger", "Queijo e bacon", new BigDecimal("38.00"),
                false, "/images/cheeseburger.jpg", 1L
        );

        when(itemService.updateItem(anyLong(), any(ItemRequest.class))).thenReturn(respostaAtualizada);

        ResponseEntity<ItemResponse> response = itemController.updateItem(1L, requisicaoAtualizada);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(respostaAtualizada, response.getBody());
        verify(itemService, times(1)).updateItem(1L, requisicaoAtualizada);
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND ao atualizar item inexistente")
    void deveRetornarNotFoundAoAtualizarItemInexistente() {
        when(itemService.updateItem(anyLong(), any(ItemRequest.class)))
                .thenThrow(new EntityNotFoundException("Item não encontrado pelo id: 99"));

        EntityNotFoundException excecao = assertThrows(EntityNotFoundException.class, () -> {
            itemController.updateItem(99L, itemRequest);
        });

        assertEquals("Item não encontrado pelo id: 99", excecao.getMessage());
        verify(itemService, times(1)).updateItem(99L, itemRequest);
    }

    @Test
    @DisplayName("Deve retornar itens por ID do restaurante")
    void deveRetornarItensPorIdDoRestaurante() {
        List<ItemResponse> respostasEsperadas = Collections.singletonList(itemResponse);
        when(itemService.getItemsByRestaurant(anyLong())).thenReturn(respostasEsperadas);

        ResponseEntity<List<ItemResponse>> response = itemController.getItemsByRestaurant(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(respostasEsperadas, response.getBody());
        verify(itemService, times(1)).getItemsByRestaurant(1L);
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND ao buscar itens de restaurante inexistente")
    void deveRetornarNotFoundAoBuscarItensDeRestauranteInexistente() {
        when(itemService.getItemsByRestaurant(anyLong()))
                .thenThrow(new EntityNotFoundException("Restaurante não encontrado pelo id: 99"));

        EntityNotFoundException excecao = assertThrows(EntityNotFoundException.class, () -> {
            itemController.getItemsByRestaurant(99L);
        });

        assertEquals("Restaurante não encontrado pelo id: 99", excecao.getMessage());
        verify(itemService, times(1)).getItemsByRestaurant(99L);
    }

    @Test
    @DisplayName("Deve deletar um item com sucesso")
    void deveDeletarItemComSucesso() {
        doNothing().when(itemService).deleteItem(anyLong());

        ResponseEntity<Void> response = itemController.deleteItem(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(itemService, times(1)).deleteItem(1L);
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND ao deletar item inexistente")
    void deveRetornarNotFoundAoDeletarItemInexistente() {
        doThrow(new EntityNotFoundException("Item não encontrado pelo id: 99"))
                .when(itemService).deleteItem(anyLong());

        EntityNotFoundException excecao = assertThrows(EntityNotFoundException.class, () -> {
            itemController.deleteItem(99L);
        });

        assertEquals("Item não encontrado pelo id: 99", excecao.getMessage());
        verify(itemService, times(1)).deleteItem(99L);
    }
}
