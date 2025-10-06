package com.example.techchallenge.infrastructure.repository;

import com.example.techchallenge.infrastructure.entities.ItemOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOrderRepository extends JpaRepository<ItemOrderEntity, Long> {
}
