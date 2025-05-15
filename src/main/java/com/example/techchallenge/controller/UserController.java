package com.example.techchallenge.controller;

import com.example.techchallenge.Request.UserRequest;
import com.example.techchallenge.Service.UserService;
import com.example.techchallenge.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            userService.createUser(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
     public ResponseEntity<String> loginUser(@RequestBody UserRequest userRequest) {
        if (userService.validateLogin(userRequest.email(), userRequest.password())) {
            return ResponseEntity.ok("Usuário Logado com Sucesso");
        } else {
            return ResponseEntity.status(401).body("Login Inválido");
        }
    }

    @GetMapping
       public ResponseEntity<List<UserEntity>> getAllUsers() throws Exception {
        List<UserEntity> listUser = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(listUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<UserEntity>> updateUser(@RequestBody UserRequest userRequest) {
        Optional<UserEntity> updatedUserEntity = userService.updateUser(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUserEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}