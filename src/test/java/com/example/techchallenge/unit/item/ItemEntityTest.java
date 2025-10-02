package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.infrastructure.entities.ItemEntity;
import com.example.techchallenge.infrastructure.entities.RestaurantEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemEntityTest {

    @Test
    void deveConverterDeDomainParaEntity() {
        Item item = new Item(1L, "Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 2L);

        ItemEntity entity = ItemEntity.fromDomain(item, null);

        assertNotNull(entity);
        assertEquals(item.getName(), entity.getName());
        assertEquals(item.getPrice(), entity.getPrice());
    }

    @Test
    void deveConverterDeEntityParaDomain() {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);
        restaurantEntity.setName("Restaurante B");

        ItemEntity entity = new ItemEntity();
        entity.setId(1L);
        entity.setName("Pizza");
        entity.setDescription("Massa fina");
        entity.setPrice(BigDecimal.valueOf(30));
        entity.setDineInOnly(false);
        entity.setPhotoPath("/img/pizza.png");
        entity.setRestaurant(restaurantEntity);

        Item domain = entity.toDomain();

        assertNotNull(domain);
        assertEquals(entity.getName(), domain.getName());
        assertEquals(entity.getPrice(), domain.getPrice());
    }

    @Test
    void deveManterNullQuandoConverterEntityNulo() {
        ItemEntity entity = null;
        assertThrows(NullPointerException.class, () -> entity.toDomain());
    }
}
