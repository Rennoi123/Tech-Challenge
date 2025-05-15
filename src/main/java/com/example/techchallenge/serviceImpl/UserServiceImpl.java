package com.example.techchallenge.serviceImpl;

import com.example.techchallenge.Enum.UserRoles;
import com.example.techchallenge.Request.UserRequest;
import com.example.techchallenge.Service.AddressService;
import com.example.techchallenge.Service.UserService;
import com.example.techchallenge.model.AddressEntity;
import com.example.techchallenge.model.UserEntity;
import com.example.techchallenge.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AddressService addressService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressService = addressService;
    }

    @Override
    public UserEntity createUser(UserRequest userRequest) {
        UserEntity entity = new UserEntity();
        entity.setName(userRequest.name());
        entity.setEmail(userRequest.email());

        insertUpdateAddress(userRequest);

        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        entity.setPassword(passwordEncoder.encode(userRequest.password()));

        entity.setRoles(UserRoles.CLIENTE);
        entity.setIsActive(true);
        return userRepository.save(entity);
    }

    private void insertUpdateAddress(UserRequest userRequest) {
        if (userRequest.address() != null) {
            AddressEntity addressEntity = addressService.getById(userRequest.id());
            addressEntity.setCity(userRequest.address().city());
            addressEntity.setComplement(userRequest.address().complement());
            addressEntity.setNumber(userRequest.address().number());
            addressEntity.setNeighborhood(userRequest.address().neighborhood());
            addressEntity.setState(userRequest.address().state());
            addressEntity.setStreet(userRequest.address().street());
            addressEntity.setPostalCode(userRequest.address().postalCode());
        }
    }

    @Override
    public Optional<UserEntity> updateUser(UserRequest userRequest) {
        return userRepository.findById(userRequest.id()).map(existingUser -> {
            existingUser.setName(userRequest.name());
            existingUser.setEmail(userRequest.email());
            existingUser.setIsActive(true);

            if (userRequest.password() != null && !userRequest.password().isBlank()) {
                existingUser.setPassword(userRequest.password());
            }
            insertUpdateAddress(userRequest);

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
    public List<UserEntity> getAll() throws Exception {
        List<UserEntity> entities = userRepository.findAll();
        if (entities.isEmpty()) {
            throw new Exception("Nenhum usuário encontrado.");
        }
        return entities;
    }
}
