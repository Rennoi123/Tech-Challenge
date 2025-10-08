package com.example.techchallenge.core.domain.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long id;
    private Long restaurantId;
    private Long userId;
    private Boolean deliveryOrder;
    private BigDecimal totalPrice;
    private String orderDate;
    private String lastModifiedDate;
    private String status;
    private List<ItemOrder> itemOrders = new ArrayList<>();


    public Order(
            Long id,
            Long restaurantId,
            Long userId,
            Boolean deliveryOrder,
            BigDecimal totalPrice,
            String orderDate,
            String lastModifiedDate,
            List<ItemOrder> itemOrders) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.deliveryOrder = deliveryOrder;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.lastModifiedDate = lastModifiedDate;
        this.itemOrders = itemOrders;
    }
}
