package com.example.techchallenge.repository;

import com.example.techchallenge.model.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AddressRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(Long id);

}