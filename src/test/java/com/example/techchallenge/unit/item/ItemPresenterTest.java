package com.example.techchallenge.unit.item;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.core.dto.ItemDTO;
import com.example.techchallenge.adapters.presenters.ItemPresenter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemPresenterTest {

    @Test
    void deveConverterDeItemParaDTO() {
        Item item = new Item(1L, "Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 1L);

        ItemDTO dto = ItemPresenter.toDTO(item);

        assertEquals(item.getName(), dto.name());
        assertEquals(item.getPrice(), dto.price());
    }

    @Test
    void deveConverterDeDTOParaItem() {
        ItemDTO dto = new ItemDTO("Pizza", "Massa fina", BigDecimal.valueOf(30), false, "/img/pizza.png", 1L);

        Item item = ItemPresenter.fromDTO(dto);

        assertEquals(dto.name(), item.getName());
        assertEquals(dto.price(), item.getPrice());
    }
}
