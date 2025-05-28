package com.example.techchallenge.service;

import com.example.techchallenge.dto.AddressRequest;
import com.example.techchallenge.dto.UserRequest;
import com.example.techchallenge.entities.AddressEntity;
import com.example.techchallenge.repository.AddressRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AddressService {

    private static final String ADDRESS_NOT_FOUND_MESSAGE = "Endereço não encontrado pelo id: ";

    private static AddressRepository addressRepository = null;

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

    private static AddressEntity buildAddressFromRequest(AddressRequest addressRequest) {
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

    public static AddressEntity createOrUpdateAddress(UserRequest userRequest) {
        AddressEntity addressEntity = buildAddressFromRequest(userRequest.address());

        if (userRequest.address().id() != null && addressRepository.existsById(userRequest.address().id())) {
            addressEntity.setId(userRequest.address().id());
        }

        return addressRepository.saveAndFlush(addressEntity);
    }

}
