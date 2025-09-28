package com.example.techchallenge.infrastructure.configuration;

import com.example.techchallenge.core.domain.usecase.address.*;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressUseCaseConfig {

    @Bean
    public CreateOrUpdateAddressUseCase createOrUpdateAddressUseCase(IAddressGateway addressGateway) {
        return new CreateOrUpdateAddressUseCase(addressGateway);
    }

    @Bean
    public GetAddressByIdUseCase getAddressByIdUseCase(IAddressGateway addressGateway) {
        return new GetAddressByIdUseCase(addressGateway);
    }

    @Bean
    public ListAddressesUseCase listAddressesUseCase(IAddressGateway addressGateway) {
        return new ListAddressesUseCase(addressGateway);
    }

    @Bean
    public DeleteAddressUseCase deleteAddressUseCase(IAddressGateway addressGateway) {
        return new DeleteAddressUseCase(addressGateway);
    }

    @Bean
    public ExistsAddressByIdUseCase existsAddressByIdUseCase(IAddressGateway addressGateway) {
        return new ExistsAddressByIdUseCase(addressGateway);
    }
}