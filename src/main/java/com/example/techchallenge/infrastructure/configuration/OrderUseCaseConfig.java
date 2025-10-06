package com.example.techchallenge.infrastructure.configuration;

import com.example.techchallenge.adapters.gateways.OrderGatewayAdapter;
import com.example.techchallenge.core.domain.usecase.itemOrder.CreateItemOrderUseCase;
import com.example.techchallenge.core.domain.usecase.order.CreateOrderUseCase;
import com.example.techchallenge.core.domain.usecase.order.GetOrderByIdUseCase;
import com.example.techchallenge.core.domain.usecase.order.ListOrdersUseCase;
import com.example.techchallenge.core.domain.usecase.order.UpdateOrderUseCase;
import com.example.techchallenge.core.interfaces.IMessagePublisher;
import com.example.techchallenge.core.interfaces.IOrderGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.infrastructure.security.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(
            IOrderGateway orderGateway,
            IRestaurantGateway restaurantGateway,
            IUserGateway userGateway,
            IMessagePublisher messagePublisher,
            CreateItemOrderUseCase createItemOrderUseCase,
            SecurityUtils securityUtils) {
        return new CreateOrderUseCase(
                orderGateway,
                restaurantGateway,
                userGateway,
                messagePublisher,
                createItemOrderUseCase,
                securityUtils);
    }

    @Bean
    public ListOrdersUseCase listOrdersUseCase(IOrderGateway orderGateway) {
        return new ListOrdersUseCase(orderGateway);
    }

    @Bean
    public UpdateOrderUseCase updateOrderUseCase(
            IOrderGateway orderGateway,
            IMessagePublisher messagePublisher,
            SecurityUtils securityUtils) {
        return new UpdateOrderUseCase(orderGateway, messagePublisher, securityUtils);
    }

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase(IOrderGateway orderGateway) {
        return new GetOrderByIdUseCase(orderGateway);
    }
}
