package com.example.techchallenge.infrastructure.repository;

import com.example.techchallenge.infrastructure.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findByRestaurantId(Long restaurantId);
    List<ItemEntity> findByNameContainingIgnoreCase(String name);
    List<ItemEntity> findByDineInOnlyFalse();
}