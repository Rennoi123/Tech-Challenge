package com.example.techchallenge.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrder {

    private Long id;
    private Long orderId;
    private Long itemId;
    private Integer quantity;
    private BigDecimal price;

    public ItemOrder(Long itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
