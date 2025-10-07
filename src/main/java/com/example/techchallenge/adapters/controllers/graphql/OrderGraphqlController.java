package com.example.techchallenge.adapters.controllers.graphql;

import com.example.techchallenge.core.domain.entities.ItemOrder;
import com.example.techchallenge.core.domain.entities.Order;
import com.example.techchallenge.core.domain.usecase.order.CreateOrderUseCase;
import com.example.techchallenge.core.domain.usecase.order.GetOrderByIdUseCase;
import com.example.techchallenge.core.domain.usecase.order.ListOrdersUseCase;
import com.example.techchallenge.core.domain.usecase.order.UpdateOrderUseCase;
import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OrderGraphqlController {

    private final ListOrdersUseCase listOrders;
    private final GetOrderByIdUseCase getOrderById;
    private final CreateOrderUseCase createOrder;
    private final UpdateOrderUseCase updateOrder;

    public OrderGraphqlController(ListOrdersUseCase listOrders,
                                  GetOrderByIdUseCase getOrderById,
                                  CreateOrderUseCase createOrder,
                                  UpdateOrderUseCase updateOrder) {
        this.listOrders = listOrders;
        this.getOrderById = getOrderById;
        this.createOrder = createOrder;
        this.updateOrder = updateOrder;
    }

    @QueryMapping
    public List<Order> orders(@Argument String status) {
        return listOrders.execute(status);
    }

    @QueryMapping
    public Order orderById(@Argument Long id) { return getOrderById.execute(id); }

    @MutationMapping
    public Order createOrder(@Argument OrderInput input) {
        Order o = new Order();
        o.setRestaurantId(input.restaurantId());
        o.setUserId(input.userId());
        o.setDeliveryOrder(input.deliveryOrder());
        for (OrderItemInput it : input.items()) {
            ItemOrder io = new ItemOrder();
            io.setItemId(it.itemId());
            io.setQuantity(it.quantity());
            o.getItemOrders().add(io);
        }
        return createOrder.execute(o);
    }

    @MutationMapping
    public Order updateOrderStatus(@Argument Long id, @Argument String status) {
        return updateOrder.execute(id, status);
    }

    public record OrderItemInput(Long itemId, Integer quantity) {}
    public record OrderInput(Long restaurantId, Long userId, Boolean deliveryOrder, List<OrderItemInput> items) {}

    @GraphQlExceptionHandler
    public GraphQLError handle(Exception ex) {
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage()).build();
    }
}
