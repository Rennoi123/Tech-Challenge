package com.example.techchallenge.adapters.gateways;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.interfaces.IAddressGateway;
import com.example.techchallenge.infrastructure.repository.AddressRepository;
import com.example.techchallenge.infrastructure.entities.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AddressGatewayAdapter implements IAddressGateway {

    private final AddressRepository repository;

    public AddressGatewayAdapter(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Address save(Address address) {
        return repository.save(AddressEntity.fromDomain(address)).toDomain();
    }

    @Override
    public Optional<Address> findById(Long id) {
        return repository.findById(id).map(AddressEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<Address> findAll() {
        return repository.findAll().stream().map(AddressEntity::toDomain).toList();
    }

    @Override
    public void delete(Address address) {
        repository.delete(AddressEntity.fromDomain(address));
    }
}
