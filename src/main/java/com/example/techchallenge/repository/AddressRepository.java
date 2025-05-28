package com.example.techchallenge.repository;

import com.example.techchallenge.entities.AddressEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findById(Long id);
    AddressEntity saveAndFlush(AddressRepository addressRepository);
}