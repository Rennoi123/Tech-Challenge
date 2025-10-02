package com.example.techchallenge.adapters.presenters;

import com.example.techchallenge.core.dto.AddressDTO;
import com.example.techchallenge.core.dto.AddressResponse;
import com.example.techchallenge.core.domain.entities.Address;

public class AddressPresenter {

    public static AddressDTO toDTO(Address address) {
        return new AddressDTO(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getPostalCode()
        );
    }

    public static AddressResponse toResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getPostalCode()
        );
    }

    public static Address fromDTO(AddressDTO dto) {
        return new Address(
                dto.id(),
                dto.street(),
                dto.number(),
                dto.complement(),
                dto.neighborhood(),
                dto.city(),
                dto.state(),
                dto.postalCode()
        );
    }
}
