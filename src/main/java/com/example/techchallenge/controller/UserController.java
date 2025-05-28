package com.example.techchallenge.controller;

import com.example.techchallenge.dto.UserRequest;
import com.example.techchallenge.dto.UserResponse;
import com.example.techchallenge.entities.UserEntity;
import com.example.techchallenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final String USER_CREATED_SUCCESS = "Usuário criado com sucesso!";
    private static final String USER_LOGGED_SUCCESS = "Usuário logado com sucesso.";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(USER_CREATED_SUCCESS);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserRequest userRequest) {
        userService.validateLogin(userRequest.email(), userRequest.password());
        return ResponseEntity.ok(USER_LOGGED_SUCCESS);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserEntity> users = userService.getAll();
        List<UserResponse> responses = users.stream()
                .map(userService::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        userRequest = new UserRequest(id, userRequest.name(), userRequest.email(), userRequest.password(), userRequest.address());
        UserEntity updatedUser = userService.updateUser(userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
