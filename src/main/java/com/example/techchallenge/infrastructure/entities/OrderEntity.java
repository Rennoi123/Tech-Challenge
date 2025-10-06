package com.example.techchallenge.infrastructure.entities;

import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.enums.StatusOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "TB_ORDER")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private RestaurantEntity restaurant;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    @Column(name = "delivery_order")
    private Boolean deliveryOrder;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "order_date")
    private LocalTime orderDate;

    @Column(name = "last_modified_date")
    private LocalTime lastModifiedDate;

    @Column(name = "status")
    private String status;

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = LocalTime.now();
    }
    @PrePersist
    protected void onCreate() {
        orderDate = LocalTime.now();
        lastModifiedDate = LocalTime.now();
        status = StatusOrder.PENDENTE.name();
    }

    public static OrderEntity fromDomain(Order order, UserEntity userEntity, RestaurantEntity restaurantEntity) {
        OrderEntity entity = new OrderEntity();
        entity.id = order.getId();
        entity.restaurant = restaurantEntity;
        entity.user = userEntity;
        entity.deliveryOrder = order.getDeliveryOrder();
        entity.totalPrice = order.getTotalPrice();
        entity.orderDate = order.getOrderDate() != null ? LocalTime.parse(order.getOrderDate()) : null;
        entity.lastModifiedDate = order.getLastModifiedDate() != null ? LocalTime.parse(order.getLastModifiedDate()) : null;
        entity.status = order.getStatus() != null ? order.getStatus() : StatusOrder.PENDENTE.name();
        return entity;
    }

    public Order toDomain() {
        return new Order(
                id,
                restaurant != null ? restaurant.getId() : null,
                user != null ? user.getId() : null,
                deliveryOrder,
                totalPrice,
                orderDate != null ? orderDate.toString() : null,
                lastModifiedDate != null ? lastModifiedDate.toString() : null,
                status != null ? status : null,
                null
        );
    }
}
