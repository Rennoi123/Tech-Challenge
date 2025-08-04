package com.example.techchallenge.repository;

import com.example.techchallenge.entities.AddressEntity;
import com.example.techchallenge.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findById(int id);

    @Query("SELECT COUNT(1) FROM RestaurantEntity r WHERE r.owner_id = :ownerId")
    int findRestaurantByOwnerId(Long ownerId);

    @Query("SELECT COUNT(1) FROM RestaurantEntity r WHERE r.address_id = :address_id")
    int findRestaurantByAddressId(Long addressId);
}