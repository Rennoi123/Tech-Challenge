package com.example.techchallenge.adapters.gateways;

import com.example.techchallenge.core.domain.entities.ItemOrder;
import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.enums.StatusOrder;
import com.example.techchallenge.core.interfaces.IOrderGateway;
import com.example.techchallenge.infrastructure.entities.OrderEntity;
import com.example.techchallenge.infrastructure.entities.RestaurantEntity;
import com.example.techchallenge.infrastructure.entities.UserEntity;
import com.example.techchallenge.infrastructure.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class OrderGatewayAdapter implements IOrderGateway {

    private static final String ORDER_ENTITY_NOT_FOUND = "Pedido não encontrado para atualizar total: ";
    private static final String ORDER_ENTITY_NOT_FOUND_TO_STATUS = "Pedido não encontrado para atualizar status: ";

    private final OrderRepository orderRepository;

    public OrderGatewayAdapter(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order, User user, Restaurant restaurant) {
        UserEntity userEntity = UserEntity.fromDomain(user);
        RestaurantEntity restaurantEntity = RestaurantEntity.fromDomain(restaurant);
        OrderEntity entity = OrderEntity.fromDomain(order, userEntity, restaurantEntity);
        return orderRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id).map(OrderEntity::toDomain);
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_ENTITY_NOT_FOUND_TO_STATUS + orderId));

        orderEntity.setStatus(status);
        OrderEntity updatedEntity = orderRepository.save(orderEntity);
        return updatedEntity.toDomain();
    }

    @Override
    public void updateOrderTotal(Long orderId, BigDecimal totalPrice) {
        OrderEntity entity = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_ENTITY_NOT_FOUND + orderId));
        entity.setTotalPrice(totalPrice);
        orderRepository.save(entity);
    }

    @Override
    public List<Order> listOrdersByStatus(String status) {
        List<OrderEntity> ordersEntity = orderRepository.findAllByStatus(status);
        return ordersEntity.stream().map(OrderEntity::toDomain).toList();
    }
}
