package com.example.techchallenge.Service;

import com.example.techchallenge.model.UserEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
     Optional<UserEntity> updateUser(UserEntity updatedData);
    UserEntity createUser(UserEntity userEntity);
    Boolean validateLogin(String login, String rawPassword);
    UserEntity getById(Long id);
    void delete(Long id);
    Page<UserEntity> getAll(Integer page, Integer size);
}
