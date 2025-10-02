package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.enums.UserRoles;
import com.example.techchallenge.infrastructure.entities.AddressEntity;
import com.example.techchallenge.infrastructure.entities.ItemEntity;
import com.example.techchallenge.adapters.gateways.ItemGatewayAdapter;
import com.example.techchallenge.infrastructure.entities.RestaurantEntity;
import com.example.techchallenge.infrastructure.entities.UserEntity;
import com.example.techchallenge.infrastructure.repository.ItemRepository;
import com.example.techchallenge.infrastructure.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemGatewayAdapterTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemGatewayAdapter itemGatewayAdapter;

    private static RestaurantEntity restaurantEntity;
    private static AddressEntity addressEntity;
    private static UserEntity userEntity;

    @BeforeAll
    static void setUp() {
        Address address = new Address(
                1L, "Rua A", "123", "Ap 1", "Centro", "Cidade X", "SP", "12345-000"
        );

        User user = new User(1L, "João", "joao@email.com", "123456", UserRoles.ADMIN, address);

        Restaurant restaurante = new Restaurant(1L, "Restaurante B", address, "Japonesa",
                LocalTime.of(9,0), LocalTime.of(23,0), 1L);

        addressEntity = new AddressEntity().fromDomain(address);
        userEntity = new UserEntity().fromDomain(user);
        restaurantEntity = RestaurantEntity.fromDomain(restaurante, addressEntity, userEntity);
    }


    @Test
    void deveSalvarItem() {
        Item item = new Item(null, "Pizza", "Massa fina", BigDecimal.valueOf(30),
                false, "/img/pizza.png", restaurantEntity.getId());

        ItemEntity savedEntity = ItemEntity.fromDomain(item, restaurantEntity);
        savedEntity.setId(1L);

        when(restaurantRepository.findById(restaurantEntity.getId())).thenReturn(Optional.of(restaurantEntity));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(savedEntity);

        Item saved = itemGatewayAdapter.save(item);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("Pizza", saved.getName());
        assertEquals("Massa fina", saved.getDescription());
        assertEquals(BigDecimal.valueOf(30), saved.getPrice());

        verify(restaurantRepository, times(1)).findById(restaurantEntity.getId());
        verify(itemRepository, times(1)).save(any(ItemEntity.class));
    }

    @Test
    void deveBuscarItemPorId() {
        ItemEntity entity = new ItemEntity();
        entity.setId(1L);
        entity.setName("Pizza");
        entity.setPrice(BigDecimal.valueOf(25.0));
        entity.setRestaurant(restaurantEntity);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Item> result = itemGatewayAdapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Pizza", result.get().getName());
        assertEquals(BigDecimal.valueOf(25.0), result.get().getPrice());
    }

    @Test
    void deveRetornarEmptyQuandoNaoEncontrarPorId() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Item> result = itemGatewayAdapter.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void deveListarItensDisponiveis() {
        ItemEntity entity = new ItemEntity();
        entity.setId(1L);
        entity.setName("Pizza");
        entity.setPrice(BigDecimal.valueOf(30.0));
        entity.setRestaurant(restaurantEntity);

        when(itemRepository.findByAvailableTrue()).thenReturn(List.of(entity));

        List<Item> result = itemGatewayAdapter.findAvailableItems();

        assertFalse(result.isEmpty());
        assertEquals("Pizza", result.get(0).getName());
        assertEquals(BigDecimal.valueOf(30.0), result.get(0).getPrice());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaItensDisponiveis() {
        when(itemRepository.findByAvailableTrue()).thenReturn(Collections.emptyList());

        List<Item> result = itemGatewayAdapter.findAvailableItems();

        assertTrue(result.isEmpty());
    }

    @Test
    void deveBuscarItensPorNome() {
        ItemEntity entity = new ItemEntity();
        entity.setId(1L);
        entity.setName("Pizza Calabresa");
        entity.setDescription("Massa fina");
        entity.setPrice(BigDecimal.valueOf(30));
        entity.setDineInOnly(false);
        entity.setPhotoPath("/img/pizza.png");
        entity.setRestaurant(restaurantEntity);

        when(itemRepository.findByNameContainingIgnoreCase("Pizza")).thenReturn(List.of(entity));

        List<Item> result = itemGatewayAdapter.findByNameContaining("Pizza");

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getName().contains("Pizza"));
    }


    @Test
    void deveRetornarListaVaziaQuandoNaoEncontrarPorNome() {
        when(itemRepository.findByNameContainingIgnoreCase("X")).thenReturn(Collections.emptyList());
        List<Item> result = itemGatewayAdapter.findByNameContaining("X");
        assertTrue(result.isEmpty());
    }

    @Test
    void deveBuscarItensPorRestaurante() {
        ItemEntity entity = new ItemEntity();
        entity.setId(1L);
        entity.setName("Pizza");
        entity.setName("Pizza Calabresa");
        entity.setDescription("Massa fina");
        entity.setPrice(BigDecimal.valueOf(30));
        entity.setDineInOnly(false);
        entity.setPhotoPath("/img/pizza.png");
        entity.setRestaurant(restaurantEntity);

        when(itemRepository.findByRestaurantId(1L)).thenReturn(List.of(entity));

        List<Item> result = itemGatewayAdapter.findByRestaurantId(1L);

        assertFalse(result.isEmpty());
    }

    @Test
    void deveRetornarListaVaziaQuandoRestauranteNaoTemItens() {
        when(itemRepository.findByRestaurantId(1L)).thenReturn(Collections.emptyList());
        List<Item> result = itemGatewayAdapter.findByRestaurantId(1L);
        assertTrue(result.isEmpty());
    }
}
