package com.example.techchallenge.core.interfaces;

import com.example.techchallenge.core.domain.entities.Restaurant;

import java.util.List;
import java.util.Optional;

public interface IRestaurantGateway {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(Long id);
    List<Restaurant> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    long countByAddressId(Long addressId);
    List<Restaurant> findByOwnerId(Long userId);
}