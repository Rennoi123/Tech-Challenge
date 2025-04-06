package com.example.techchallenge.serviceImpl;

import com.example.techchallenge.Service.UserService;
import com.example.techchallenge.mapper.UserMapper;
import com.example.techchallenge.model.UserEntity;
import com.example.techchallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper mapper;

    private PasswordEncoder passwordEncoder;



    @Override
    public UserEntity createUser(UserEntity userEntity) {
        UserEntity entity = mapper.create(userEntity);
        entity.setIsActive(true);
        return userRepository.save(userEntity);
    }
    @Override
    public Optional<UserEntity> updateUser(UserEntity updatedData) {
        return userRepository.findById(updatedData.getId()).map(existingUser -> {
            existingUser.setName(updatedData.getName());
            existingUser.setEmail(updatedData.getEmail());
            existingUser.setLogin(updatedData.getLogin());
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
    public Boolean validateLogin(String login, String rawPassword) {
        return userRepository.findByLogin(login)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .isPresent();
    }


    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado pelo id: " + id));
    }

    @Override
    public Page<UserEntity> getAll(Integer page, Integer size) {
        Pageable pageable = page == null || size == null ? Pageable.unpaged() : PageRequest.of(page, size);
        Page<UserEntity> entities = userRepository.findAll(pageable);
        if (entities.isEmpty()) {
            return null;
        }

        return entities;
    }

}