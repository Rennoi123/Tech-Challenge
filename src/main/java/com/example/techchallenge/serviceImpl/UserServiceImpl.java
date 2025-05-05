package com.example.techchallenge.serviceImpl;

import com.example.techchallenge.Enum.UserRoles;
import com.example.techchallenge.Service.UserService;
import com.example.techchallenge.mapper.UserMapper;
import com.example.techchallenge.model.UserEntity;
import com.example.techchallenge.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        UserEntity entity = mapper.create(userEntity);

        if (userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        entity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        entity.setRoles(UserRoles.CLIENTE);
        entity.setIsActive(true);
        return userRepository.save(entity);
    }

    @Override
    public Optional<UserEntity> updateUser(UserEntity updatedData) {
        return userRepository.findById(updatedData.getId()).map(existingUser -> {
            existingUser.setName(updatedData.getName());
            existingUser.setEmail(updatedData.getEmail());
            existingUser.setIsActive(true);

            if (updatedData.getPassword() != null && !updatedData.getPassword().isBlank()) {
                existingUser.setPassword(updatedData.getPassword());
            }

            existingUser.setAddress(updatedData.getAddress());

            return userRepository.save(existingUser);
        });
    }

    @Override
    public void delete(Long id) {
        UserEntity entity = getById(id);
        entity.setIsActive(false);
        userRepository.save(entity);
    }

    @Override
    public Boolean validateLogin(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado pelo id: " + id));
    }

    @Override
    public Page<UserEntity> getAll(Integer page, Integer size) {
        Pageable pageable = page == null || size == null ? Pageable.unpaged() : PageRequest.of(page, size);
        Page<UserEntity> entities = userRepository.findAll(pageable);
        return entities.isEmpty() ? null : entities;
    }
}
