package com.example.techchallenge.repository;


import com.example.techchallenge.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findById(Long id);


    Page<UserEntity> findAll(Pageable pageable);

    Optional<UserEntity> findByLoginAndPassword(String login, String password);
}