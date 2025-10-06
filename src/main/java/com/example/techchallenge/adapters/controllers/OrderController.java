package com.example.techchallenge.adapters.controllers;

import com.example.techchallenge.adapters.presenters.OrderPresenter;
import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.domain.usecase.order.*;
import com.example.techchallenge.core.dto.OrderDTO;
import com.example.techchallenge.core.dto.OrderResponse;
import com.example.techchallenge.core.enums.StatusOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;

    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetOrderByIdUseCase getOrderByIdUseCase,
            UpdateOrderUseCase updateOrderUseCase,
            ListOrdersUseCase listOrdersUseCase) {
        this.listOrdersUseCase = listOrdersUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.createOrderUseCase = createOrderUseCase;
    }

    @GetMapping("/cancelled")
    public ResponseEntity<List<OrderResponse>> listCancelledOrders() {
        List<OrderResponse> orders = listOrdersUseCase.execute(StatusOrder.CANCELADO.name())
                .stream()
                .map(OrderPresenter::ToResponse)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OrderResponse>> listPendingOrders() {
        List<OrderResponse> orders = listOrdersUseCase.execute(StatusOrder.PENDENTE.name())
                .stream()
                .map(OrderPresenter::ToResponse)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/preparing")
    public ResponseEntity<List<OrderResponse>> listPreparingOrders() {
        List<OrderResponse> orders = listOrdersUseCase.execute(StatusOrder.EM_PREPARO.name())
                .stream()
                .map(OrderPresenter::ToResponse)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<OrderResponse>> listCompletedOrders() {
        List<OrderResponse> orders = listOrdersUseCase.execute(StatusOrder.FINALIZADO.name())
                .stream()
                .map(OrderPresenter::ToResponse)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderDTO orderDTO) {
        Order order = OrderPresenter.fromDTO(orderDTO);
        OrderResponse response = OrderPresenter.ToResponse(createOrderUseCase.execute(order));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        Order order = getOrderByIdUseCase.execute(id);
        return ResponseEntity.ok(OrderPresenter.ToResponse(order));
    }

    @PutMapping("/{id}/preparing")
    public ResponseEntity<OrderResponse> prepareOrder(@PathVariable Long id) {
        Order order = updateOrderUseCase.execute(id, StatusOrder.EM_PREPARO.name());
        return ResponseEntity.ok(OrderPresenter.ToResponse(order));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<OrderResponse> completeOrder(@PathVariable Long id) {
        Order order = updateOrderUseCase.execute(id, StatusOrder.FINALIZADO.name());
        return ResponseEntity.ok(OrderPresenter.ToResponse(order));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        Order order = updateOrderUseCase.execute(id, StatusOrder.CANCELADO.name());
        return ResponseEntity.ok(OrderPresenter.ToResponse(order));
    }
}
