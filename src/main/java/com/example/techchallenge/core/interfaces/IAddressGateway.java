package com.example.techchallenge.core.interfaces;

import com.example.techchallenge.core.domain.entities.Address;

import java.util.List;
import java.util.Optional;

public interface IAddressGateway {
    Address save(Address address);
    Optional<Address> findById(Long id);
    boolean existsById(Long id);
    List<Address> findAll();
    void delete(Address address);
}