package com.example.techchallenge.core.domain.usecase.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.interfaces.IAddressGateway;

import java.util.List;

public class ListAddressesUseCase {

    private final IAddressGateway addressGateway;

    public ListAddressesUseCase(IAddressGateway addressGateway) {
        this.addressGateway = addressGateway;
    }

    public List<Address> execute() {
        return addressGateway.findAll();
    }
}
