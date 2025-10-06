package com.example.techchallenge.infrastructure.repository;

import com.example.techchallenge.infrastructure.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findById(Long id);
    List<OrderEntity> findAllByStatus(String status);
}
