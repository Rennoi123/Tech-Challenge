package com.example.techchallenge.adapters.gateways;

import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.infrastructure.entities.UserEntity;
import com.example.techchallenge.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class UserGatewayAdapter implements IUserGateway {

    private final UserRepository userRepository;

    public UserGatewayAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User u) {
        return userRepository.save(UserEntity.fromDomain(u)).toDomain();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().map(UserEntity::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
