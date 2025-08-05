package com.example.techchallenge.repository;

import com.example.techchallenge.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findById(int id);

    int countByOwnerId(Long ownerId);

    int countByAddressId(Long addressId);
}