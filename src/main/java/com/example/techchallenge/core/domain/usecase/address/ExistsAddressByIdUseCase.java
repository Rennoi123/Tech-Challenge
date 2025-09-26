package com.example.techchallenge.core.domain.usecase.address;

import com.example.techchallenge.core.interfaces.IAddressGateway;

public class ExistsAddressByIdUseCase {

    private final IAddressGateway addressGateway;

    public ExistsAddressByIdUseCase(IAddressGateway addressGateway) {
        this.addressGateway = addressGateway;
    }

    public boolean execute(Long id) {
        return addressGateway.existsById(id);
    }
}
