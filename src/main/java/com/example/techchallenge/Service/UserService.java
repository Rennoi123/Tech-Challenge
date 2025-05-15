package com.example.techchallenge.Service;

import com.example.techchallenge.Request.UserRequest;
import com.example.techchallenge.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public interface UserService {
     Optional<UserEntity> updateUser(UserRequest userRequest);
    UserEntity createUser(UserRequest userRequest);
    Boolean validateLogin(String username, String rawPassword);
    UserEntity getById(Long id);
    void delete(Long id);
    List<UserEntity> getAll() throws Exception;
}
