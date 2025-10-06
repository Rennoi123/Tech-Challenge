package com.example.techchallenge.infrastructure.repository;

import com.example.techchallenge.infrastructure.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findById(int id);
    int countByAddressId(Long addressId);
    boolean existsByAddressId(Long addressId);
}