package com.example.techchallenge.infrastructure.configuration;

import com.example.techchallenge.core.domain.usecase.itemOrder.CreateItemOrderUseCase;
import com.example.techchallenge.core.interfaces.IItemGateway;
import com.example.techchallenge.core.interfaces.IItemOrderGateway;
import com.example.techchallenge.core.interfaces.IOrderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ItemOrderUseCaseConfig {

    @Bean
    public CreateItemOrderUseCase createItemOrderUseCase(
            IItemOrderGateway itemOrderGateway,
            IOrderGateway orderGateway,
            IItemGateway itemGateway) {
        return new CreateItemOrderUseCase(itemOrderGateway, orderGateway, itemGateway);
    }
}
