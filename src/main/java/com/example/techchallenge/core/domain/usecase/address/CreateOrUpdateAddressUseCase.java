package com.example.techchallenge.core.domain.usecase.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.interfaces.IAddressGateway;

public class CreateOrUpdateAddressUseCase {

    private final IAddressGateway addressGateway;

    public CreateOrUpdateAddressUseCase(IAddressGateway addressGateway) {
        this.addressGateway = addressGateway;
    }

    public Address execute(Address address) {
        address.validate();

        if (address.getId() != null && addressGateway.existsById(address.getId())) {
            return addressGateway.save(address);
        }

        return addressGateway.save(address);
    }
}
