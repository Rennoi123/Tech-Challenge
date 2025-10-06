package com.example.techchallenge.infrastructure.configuration;

import com.example.techchallenge.core.domain.usecase.reservation.CreateReservationUseCase;
import com.example.techchallenge.core.domain.usecase.reservation.ListReservationsByRestaurantOwnerUseCase;
import com.example.techchallenge.core.domain.usecase.reservation.UpdateReservationStatusUseCase;
import com.example.techchallenge.core.interfaces.IReservationGateway;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.core.interfaces.ISecurityGateway;
import com.example.techchallenge.core.interfaces.IUserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservationUseCaseConfig {

    @Bean
    public CreateReservationUseCase createReservationUseCase(
            IReservationGateway reservationGateway,
            IRestaurantGateway restaurantGateway,
            IUserGateway userGateway,
            ISecurityGateway securityGateway
    ) {
        return new CreateReservationUseCase(reservationGateway, restaurantGateway, userGateway, securityGateway);
    }


    @Bean
    public UpdateReservationStatusUseCase updateReservationStatusUseCase(IReservationGateway reservationGateway,
                                                                         IRestaurantGateway restaurantGateway,
                                                                         ISecurityGateway securityGateway,
                                                                         IUserGateway userGateway) {
        return new UpdateReservationStatusUseCase(reservationGateway, restaurantGateway, securityGateway, userGateway);
    }

    @Bean
    public ListReservationsByRestaurantOwnerUseCase listReservationsByRestaurantOwnerUseCase(
            IReservationGateway reservationGateway,
            IRestaurantGateway restaurantGateway,
            IUserGateway userGateway,
            ISecurityGateway securityGateway
    ) {
        return new ListReservationsByRestaurantOwnerUseCase(reservationGateway, restaurantGateway, userGateway, securityGateway);
    }

}