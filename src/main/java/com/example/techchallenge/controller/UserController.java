package com.example.techchallenge.controller;

import com.example.techchallenge.Request.UserRequest;
import com.example.techchallenge.Response.UserResponse;
import com.example.techchallenge.mapper.ModelMapperBase;
import com.example.techchallenge.model.UserEntity;
import com.example.techchallenge.serviceImpl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;


    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            userServiceImpl.createUser(userRequest.toEntity());
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
     public ResponseEntity<String> loginUser(@RequestBody UserRequest userRequest) {
        if (userServiceImpl.validateLogin(userRequest.email(), userRequest.password())) {
            return ResponseEntity.ok("Usuário Logado com Sucesso");
        } else {
            return ResponseEntity.status(401).body("Login Inválido");
        }
    }

    @GetMapping
       public ResponseEntity<Page<UserEntity>> getAllUsers(@RequestParam(value = "page",required = false) Integer page,
                                                                  @RequestParam(value = "size", required = false) Integer size) {
        Page<UserEntity> pages = userServiceImpl.getAll(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(pages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        Optional<UserEntity> updatedUserEntity = userServiceImpl.updateUser(userRequest.toEntity());
        return ResponseEntity.status(HttpStatus.OK).body(ModelMapperBase.map(updatedUserEntity, UserResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }

}