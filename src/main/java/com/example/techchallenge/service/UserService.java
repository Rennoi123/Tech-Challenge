package com.example.techchallenge.service;

import com.example.techchallenge.dto.Response.AddressResponse;
import com.example.techchallenge.dto.Response.UserResponse;
import com.example.techchallenge.enums.UserRoles;
import com.example.techchallenge.dto.Request.UserRequest;
import com.example.techchallenge.exception.UserNotFoundException;
import com.example.techchallenge.exception.InvalidCredentialsException;
import com.example.techchallenge.entities.AddressEntity;
import com.example.techchallenge.entities.UserEntity;
import com.example.techchallenge.repository.UserRepository;
import com.example.techchallenge.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private static final String USER_NOT_FOUND_MESSAGE_BY_ID = "Usuário não encontrado pelo id: ";
    private static final String EMAIL_NOT_FOUND_MESSAGE = "Email não encontrado.";
    private static final String INVALID_PASSWORD_MESSAGE = "Senha inválida para o email informado.";
    private static final String INVALID_PASSWORD_ATUAL = "Senha atual incorreta.";
    private static final String EMPTY_EMAIL_MESSAGE = "Email não pode ser vazio.";
    private static final String EMPTY_PASSWORD_MESSAGE = "Senha não pode ser vazia.";
    private static final String EMPTY_NAME_MESSAGE = "Nome não pode ser vazio.";
    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email já está em uso.";
    private static final String USER_CANNOT_BE_DELETED_OWNER_RESTAURANT = "Usuário não pode ser excluído, pois é dono de um restaurante.";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AddressService addressService, RestaurantService restaurantService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressService = addressService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserEntity createUser(UserRequest userRequest, String userRoles) {
        validateUserRequest(userRequest);

        if (emailExists(userRequest.email())) {
            throw new IllegalArgumentException(EMAIL_ALREADY_EXISTS_MESSAGE);
        }

        UserEntity newUser = buildUserFromRequest(userRequest);

        if(userRoles != null){
            newUser.setRoles(UserRoles.ADMIN);
        }
        return userRepository.save(newUser);
    }

    public UserEntity createUser(UserRequest userRequest) {
        return createUser(userRequest, null);
    }

    public UserResponse updateUser(UserRequest userRequest) {
        validateUserRequest(userRequest);
        UserEntity existingUser = getUserById(userRequest.id());
        updateUserData(existingUser, userRequest);
        UserEntity userEntity = userRepository.save(existingUser);
        return toResponse(userEntity);
    }

    public void delete(Long id) {
        UserEntity entity = getUserById(id);

        userRepository.delete(entity);
    }

    public Boolean validateLogin(String email, String rawPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(EMAIL_NOT_FOUND_MESSAGE));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException(INVALID_PASSWORD_MESSAGE);
        }

        return true;
    }

    public String loginAndGetToken(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(EMAIL_NOT_FOUND_MESSAGE));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException(INVALID_PASSWORD_MESSAGE);
        }

        return jwtTokenProvider.generateToken(user);
    }

    public UserEntity getById(Long id) {
        return getUserById(id);
    }

    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException(EMPTY_PASSWORD_MESSAGE);
        }

        UserEntity user = getUserById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidCredentialsException(INVALID_PASSWORD_ATUAL);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<UserEntity> getAll() {
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            return Collections.emptyList();
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
            addressResponse,
            user.getRoles()
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

        if (userRequest.address() != null) {
            AddressEntity address = addressService.createOrUpdateAddress(userRequest.address());
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
            AddressEntity address = addressService.createOrUpdateAddress(userRequest.address());
            user.setAddress(address);
        }
    }

    private UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + id));
    }
}