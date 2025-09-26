package com.example.techchallenge.core.domain.usecase.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.exception.AddressNotFoundException;
import com.example.techchallenge.core.interfaces.IAddressGateway;

public class DeleteAddressUseCase {

    private static final String ADDRESS_NOT_FOUND_MSG = "Endereço não encontrado pelo id: ";

    private final IAddressGateway addressGateway;

    public DeleteAddressUseCase(IAddressGateway addressGateway) {
        this.addressGateway = addressGateway;
    }

    public void execute(Long id) {
        Address address = addressGateway.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(ADDRESS_NOT_FOUND_MSG + id));
        addressGateway.delete(address);
    }
}
