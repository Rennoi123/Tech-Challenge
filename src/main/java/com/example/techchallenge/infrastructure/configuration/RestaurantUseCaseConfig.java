package com.example.techchallenge.infrastructure.configuration;


import com.example.techchallenge.core.domain.usecase.restaurant.CreateRestaurantUseCase;
import com.example.techchallenge.core.domain.usecase.restaurant.DeleteRestaurantUseCase;
import com.example.techchallenge.core.domain.usecase.restaurant.GetRestaurantByIdUseCase;
import com.example.techchallenge.core.domain.usecase.restaurant.ListRestaurantsUseCase;
import com.example.techchallenge.core.domain.usecase.restaurant.UpdateRestaurantUseCase;
import com.example.techchallenge.core.interfaces.IAddressGateway;

import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantUseCaseConfig {


    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(IRestaurantGateway restaurantGateway,
                                                     IAddressGateway addressGateway,
                                                     IUserGateway userGateway,
                                                     ISecurityGateway securityGateway) {
        return new CreateRestaurantUseCase(restaurantGateway, addressGateway, userGateway, securityGateway);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(IRestaurantGateway restaurantGateway) {
        return new DeleteRestaurantUseCase(restaurantGateway);
    }


    @Bean
    public GetRestaurantByIdUseCase getRestaurantByIdUseCase(IRestaurantGateway restaurantGateway) {
        return new GetRestaurantByIdUseCase(restaurantGateway);
    }

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(IRestaurantGateway restaurantGateway) {
        return new ListRestaurantsUseCase(restaurantGateway);
    }


    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(IRestaurantGateway restaurantGateway) {
        return new UpdateRestaurantUseCase(restaurantGateway);
    }
}
