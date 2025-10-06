package com.example.techchallenge.infrastructure.entities;

import com.example.techchallenge.core.domain.entities.ItemOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "TB_ITEMS_ORDER")
public class ItemOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private OrderEntity order;

    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private ItemEntity item;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigDecimal price;

    public static ItemOrderEntity fromDomain(ItemOrder itemOrder, ItemEntity itemEntity, OrderEntity orderEntity) {
        ItemOrderEntity entity = new ItemOrderEntity();
        entity.id = orderEntity.getId();
        entity.order = orderEntity;
        entity.item = itemEntity;
        entity.quantity = itemOrder.getQuantity();
        entity.price = itemOrder.getPrice();
        return entity;
    }

    public ItemOrder toDomain() {
        return new ItemOrder(id,
                order != null ? order.getId() : null,
                item != null ? item.getId() : null,
                quantity,
                price);
    }
}
