package com.example.techchallenge.service;

import com.example.techchallenge.dto.AddressResponse;
import com.example.techchallenge.dto.UserResponse;
import com.example.techchallenge.enums.UserRoles;
import com.example.techchallenge.dto.UserRequest;
import com.example.techchallenge.exception.UserNotFoundException;
import com.example.techchallenge.exception.InvalidCredentialsException;
import com.example.techchallenge.entities.AddressEntity;
import com.example.techchallenge.entities.UserEntity;
import com.example.techchallenge.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "Nenhum usuário encontrado.";
    private static final String USER_NOT_FOUND_MESSAGE_BY_ID = "Usuário não encontrado pelo id: ";
    private static final String EMAIL_NOT_FOUND_MESSAGE = "Email não encontrado.";
    private static final String INVALID_PASSWORD_MESSAGE = "Senha inválida para o email informado.";
    private static final String EMPTY_EMAIL_MESSAGE = "Email não pode ser vazio.";
    private static final String EMPTY_PASSWORD_MESSAGE = "Senha não pode ser vazia.";
    private static final String EMPTY_NAME_MESSAGE = "Nome não pode ser vazio.";
    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email já está em uso.";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AddressService addressService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressService = addressService;
    }

    public UserEntity createUser(UserRequest userRequest) {
        validateUserRequest(userRequest);

        if (emailExists(userRequest.email())) {
            throw new IllegalArgumentException(EMAIL_ALREADY_EXISTS_MESSAGE);
        }

        UserEntity newUser = buildUserFromRequest(userRequest);
        return userRepository.save(newUser);
    }

    public UserEntity updateUser(UserRequest userRequest) {
        validateUserRequest(userRequest);
        UserEntity existingUser = getUserById(userRequest.id());
        updateUserData(existingUser, userRequest);
        return userRepository.save(existingUser);
    }

    public void delete(Long id) {
        UserEntity entity = getUserById(id);
        entity.setIsActive(false);
        userRepository.save(entity);
    }

    public Boolean validateLogin(String email, String rawPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(EMAIL_NOT_FOUND_MESSAGE));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException(INVALID_PASSWORD_MESSAGE);
        }

        return true;
    }

    public UserEntity getById(Long id) {
        return getUserById(id);
    }

    public List<UserEntity> getAll() {
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return users;
    }

    private void validateUserRequest(UserRequest userRequest) {
        if (userRequest.email() == null || userRequest.email().isBlank()) {
            throw new IllegalArgumentException(EMPTY_EMAIL_MESSAGE);
        }
        if (userRequest.password() == null || userRequest.password().isBlank()) {
            throw new IllegalArgumentException(EMPTY_PASSWORD_MESSAGE);
        }
        if (userRequest.name() == null || userRequest.name().isBlank()) {
            throw new IllegalArgumentException(EMPTY_NAME_MESSAGE);
        }
    }

    public UserResponse toResponse(UserEntity user) {
        AddressEntity address = user.getAddress();
        AddressResponse addressResponse = null;

        if (address != null) {
            addressResponse = new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getPostalCode()
            );
        }

        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            addressResponse
        );
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private UserEntity buildUserFromRequest(UserRequest userRequest) {
        UserEntity user = new UserEntity();
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setRoles(UserRoles.CLIENTE);
        user.setIsActive(true);

        if (userRequest.address() != null) {
            AddressEntity address = AddressService.createOrUpdateAddress(userRequest);
            user.setAddress(address);
        }

        return user;
    }

    private void updateUserData(UserEntity user, UserRequest userRequest) {
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());

        if (userRequest.password() != null && !userRequest.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userRequest.password()));
        }

        if (userRequest.address() != null) {
            AddressEntity address = AddressService.createOrUpdateAddress(userRequest);
            user.setAddress(address);
        }
    }

    private UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + id));
    }
}