package com.example.techchallenge.unit.service;

import com.example.techchallenge.dto.Request.ItemRequest;
import com.example.techchallenge.dto.Response.ItemResponse;
import com.example.techchallenge.entities.ItemEntity;
import com.example.techchallenge.entities.RestaurantEntity;
import com.example.techchallenge.repository.ItemRepository;
import com.example.techchallenge.repository.RestaurantRepository;
import com.example.techchallenge.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ItemService itemService;

    private ItemRequest itemRequest;
    private ItemEntity itemEntity;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Restaurante Mock");
        restaurantEntity.setCuisineType("Italiana");
        restaurantEntity.setOpeningTime(LocalTime.of(9, 0));
        restaurantEntity.setClosingTime(LocalTime.of(22, 0));

        itemRequest = new ItemRequest(
        "Pizza", "Pizza de Pepperoni", new BigDecimal("50.00"),
        true, "/images/pizza.jpg", 1L
        );

        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("Pizza");
        itemEntity.setDescription("Pizza de Pepperoni");
        itemEntity.setPrice(new BigDecimal("50.00"));
        itemEntity.setDineInOnly(true);
        itemEntity.setPhotoPath("/images/pizza.jpg");
        itemEntity.setRestaurant(restaurantEntity);
    }

    @Test
    @DisplayName("Deve criar um item com sucesso")
    void deveCriarItemComSucesso() {
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurantEntity));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(itemEntity);

        ItemResponse response = itemService.createItem(itemRequest);

        assertNotNull(response);
        assertEquals(itemRequest.name(), response.name());
        assertEquals(itemRequest.price(), response.price());
        assertEquals(itemRequest.restaurantId(), response.restaurantId());
        verify(restaurantRepository, times(1)).findById(itemRequest.restaurantId());
        verify(itemRepository, times(1)).save(any(ItemEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não encontrado na criação do item")
    void deveLancarExcecaoQuandoRestauranteNaoEncontradoNaCriacaoDoItem() {
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            itemService.createItem(itemRequest);
        });

        assertEquals("Restaurante não encontrado pelo id: " + itemRequest.restaurantId(), exception.getMessage());
        verify(restaurantRepository, times(1)).findById(itemRequest.restaurantId());
        verify(itemRepository, never()).save(any(ItemEntity.class));
    }

    @Test
    @DisplayName("Deve atualizar um item com sucesso")
    void deveAtualizarItemComSucesso() {
        ItemRequest updateRequest = new ItemRequest(
        "Pizza Margherita", "Pizza clássica", new BigDecimal("45.00"),
        false, "/images/margherita.jpg", 1L
        );

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemEntity));
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurantEntity));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(itemEntity);

        ItemResponse response = itemService.updateItem(1L, updateRequest);

        assertNotNull(response);
        assertEquals(updateRequest.name(), response.name());
        assertEquals(updateRequest.price(), response.price());
        assertFalse(response.dineInOnly());
        verify(itemRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).findById(updateRequest.restaurantId());
        verify(itemRepository, times(1)).save(any(ItemEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando item não encontrado na atualização")
    void deveLancarExcecaoQuandoItemNaoEncontradoNaAtualizacao() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            itemService.updateItem(99L, itemRequest);
        });

        assertEquals("Item não encontrado pelo id: 99", exception.getMessage());
        verify(itemRepository, times(1)).findById(99L);
        verify(restaurantRepository, never()).findById(anyLong());
        verify(itemRepository, never()).save(any(ItemEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não encontrado na atualização do item")
    void deveLancarExcecaoQuandoRestauranteNaoEncontradoNaAtualizacaoDoItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemEntity));
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        ItemRequest updateRequestComRestauranteInvalido = new ItemRequest(
            "Pizza", "Desc", new BigDecimal("10"), true, "path", 99L
        );

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            itemService.updateItem(1L, updateRequestComRestauranteInvalido);
        });

        assertEquals("Restaurante não encontrado pelo id: 99", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).findById(99L);
        verify(itemRepository, never()).save(any(ItemEntity.class));
    }

    @Test
    @DisplayName("Deve retornar lista de itens por restaurante")
    void deveRetornarListaDeItensPorRestaurante() {
        ItemEntity item2 = new ItemEntity();
        item2.setId(2L);
        item2.setName("Lasanha");
        item2.setRestaurant(restaurantEntity);

        List<ItemEntity> items = Arrays.asList(itemEntity, item2);
        when(restaurantRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findByRestaurantId(anyLong())).thenReturn(items);

        List<ItemResponse> responses = itemService.getItemsByRestaurant(1L);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Pizza", responses.get(0).name());
        assertEquals("Lasanha", responses.get(1).name());
        verify(restaurantRepository, times(1)).existsById(1L);
        verify(itemRepository, times(1)).findByRestaurantId(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar itens de restaurante inexistente")
    void deveLancarExcecaoAoBuscarItensDeRestauranteInexistente() {
        when(restaurantRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            itemService.getItemsByRestaurant(99L);
        });

        assertEquals("Restaurante não encontrado pelo id: 99", exception.getMessage());
        verify(restaurantRepository, times(1)).existsById(99L);
        verify(itemRepository, never()).findByRestaurantId(anyLong());
    }

    @Test
    @DisplayName("Deve deletar um item com sucesso")
    void deveDeletarItemComSucesso() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(itemRepository).deleteById(anyLong());

        itemService.deleteItem(1L);

        verify(itemRepository, times(1)).existsById(1L);
        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar item inexistente")
    void deveLancarExcecaoAoTentarDeletarItemInexistente() {
        when(itemRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            itemService.deleteItem(99L);
        });

        assertEquals("Item não encontrado pelo id: 99", exception.getMessage());
        verify(itemRepository, times(1)).existsById(99L);
        verify(itemRepository, never()).deleteById(anyLong());
    }
}
