package com.example.techchallenge.controller;

import com.example.techchallenge.dto.UpdatePasswrodRequest;
import com.example.techchallenge.dto.UserRequest;
import com.example.techchallenge.dto.UserResponse;
import com.example.techchallenge.entities.UserEntity;
import com.example.techchallenge.enums.UserRoles;
import com.example.techchallenge.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final String USER_CREATED_SUCCESS = "Usuário criado com sucesso!";
    private static final String USER_LOGGED_SUCCESS = "Usuário logado com sucesso.";
    private static final String PASSWORD_UPDATED_SUCCESS = "Senha atualizada com sucesso.";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(USER_CREATED_SUCCESS);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<String> createUserAdmin(@RequestBody UserRequest userRequest) {
        userRequest.setUserRoles(UserRoles.RESTAURANTE);
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
        List<UserResponse> userResponses = users.stream()
                .map(userService::toResponse)
                .toList();

        return ResponseEntity.ok(userResponses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getById(id);
        UserResponse userResponse = userService.toResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        userRequest = new UserRequest(id, userRequest.name(), userRequest.email(), userRequest.password(), userRequest.address(), null);
        UserResponse userResponse = userService.updateUser(userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestParam Long id, @RequestBody UpdatePasswrodRequest updatePasswrodRequest) {
        userService.updatePassword(id, updatePasswrodRequest.oldPassword(), updatePasswrodRequest.newPassword());
        return ResponseEntity.ok(PASSWORD_UPDATED_SUCCESS );
    }
}
