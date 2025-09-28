package com.example.techchallenge.infrastructure.configuration;

import com.example.techchallenge.core.domain.usecase.item.CreateItemUseCase;
import com.example.techchallenge.core.domain.usecase.item.DeleteItemUseCase;
import com.example.techchallenge.core.domain.usecase.item.FindItemsByNameUseCase;
import com.example.techchallenge.core.domain.usecase.item.FindItemsByRestaurantUseCase;
import com.example.techchallenge.core.domain.usecase.item.GetItemByIdUseCase;
import com.example.techchallenge.core.domain.usecase.item.ListItemsUseCase;
import com.example.techchallenge.core.domain.usecase.item.UpdateItemUseCase;
import com.example.techchallenge.core.interfaces.IItemGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemUseCaseConfig {

    @Bean
    public CreateItemUseCase createItemUseCase(IItemGateway itemGateway, IRestaurantGateway restaurantGateway) {
        return new CreateItemUseCase(itemGateway, restaurantGateway);
    }

    @Bean
    public DeleteItemUseCase deleteItemUseCase(IItemGateway itemGateway) {
        return new DeleteItemUseCase(itemGateway);
    }

    @Bean
    public FindItemsByNameUseCase findItemsByNameUseCase(IItemGateway itemGateway) {
        return new FindItemsByNameUseCase(itemGateway);
    }

    @Bean
    public FindItemsByRestaurantUseCase findItemsByRestaurantUseCase(IItemGateway itemGateway) {
        return new FindItemsByRestaurantUseCase(itemGateway);
    }

    @Bean
    public GetItemByIdUseCase getItemByIdUseCase(IItemGateway itemGateway) {
        return new GetItemByIdUseCase(itemGateway);
    }

    @Bean
    public ListItemsUseCase listItemsUseCase(IItemGateway itemGateway) {
        return new ListItemsUseCase(itemGateway);
    }

    @Bean
    public UpdateItemUseCase updateItemUseCase(IItemGateway itemGateway, IRestaurantGateway restaurantGateway) {
        return new UpdateItemUseCase(itemGateway, restaurantGateway);
    }
}