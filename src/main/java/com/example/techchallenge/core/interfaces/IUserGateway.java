package com.example.techchallenge.core.interfaces;

import com.example.techchallenge.core.domain.entities.User;
import java.util.*;

public interface IUserGateway {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(Long id);
}
