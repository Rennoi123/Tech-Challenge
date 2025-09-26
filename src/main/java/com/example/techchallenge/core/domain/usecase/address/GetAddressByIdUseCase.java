package com.example.techchallenge.core.domain.usecase.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import com.example.techchallenge.core.exception.AddressNotFoundException;

public class GetAddressByIdUseCase {

    private static final String ADDRESS_NOT_FOUND_MESSAGE = "Endereço não encontrado pelo id: ";

    private final IAddressGateway addressGateway;

    public GetAddressByIdUseCase(IAddressGateway addressGateway) {
        this.addressGateway = addressGateway;
    }

    public Address execute(Long id) {
        return addressGateway.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(ADDRESS_NOT_FOUND_MESSAGE + id));
    }
}
