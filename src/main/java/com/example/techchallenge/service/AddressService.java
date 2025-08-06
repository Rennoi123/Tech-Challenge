package com.example.techchallenge.service;

import com.example.techchallenge.dto.Request.AddressRequest;
import com.example.techchallenge.entities.AddressEntity;
import com.example.techchallenge.repository.AddressRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AddressService {

    private static final String ADDRESS_NOT_FOUND_MESSAGE = "Endereço não encontrado pelo id: ";

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public AddressEntity getById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ADDRESS_NOT_FOUND_MESSAGE + id));
    }

    public boolean existsById(Long id) {
        return addressRepository.existsById(id);
    }

    private AddressEntity buildAddressFromRequest(AddressRequest addressRequest) {
        AddressEntity address = new AddressEntity();
        address.setCity(addressRequest.city());
        address.setComplement(addressRequest.complement());
        address.setNumber(addressRequest.number());
        address.setNeighborhood(addressRequest.neighborhood());
        address.setState(addressRequest.state());
        address.setStreet(addressRequest.street());
        address.setPostalCode(addressRequest.postalCode());


        return address;
    }

    public AddressEntity createOrUpdateAddress(AddressRequest addressRequest) {
        AddressEntity addressEntity = buildAddressFromRequest(addressRequest);

        if (addressRequest.id() != null && addressRepository.existsById(addressRequest.id())) {
            addressEntity.setId(addressRequest.id());
        }

        return addressRepository.save(addressEntity);
    }

}
