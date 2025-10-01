package com.example.techchallenge.adapters.controllers;

import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.domain.usecase.user.*;
import com.example.techchallenge.core.dto.UpdatePasswordDTO;
import com.example.techchallenge.core.enums.UserRoles;
import com.example.techchallenge.core.exception.InvalidCredentialsException;
import com.example.techchallenge.infrastructure.entities.UserEntity;
import com.example.techchallenge.infrastructure.security.JwtTokenProvider;
import com.example.techchallenge.core.dto.UserDTO;
import com.example.techchallenge.adapters.presenters.UserPresenter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final String INVALID_CREDENTIALS = "Credenciais inválidas.";
    private static final String PASSWORD_UPDATED_SUCCESS = "Senha atualizada com sucesso!";

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UpdatePasswordUseCase updatePasswordUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(CreateUserUseCase createUserUseCase, GetUserUseCase getUserUseCase, ListUsersUseCase listUsersUseCase,
                          UpdateUserUseCase updateUserUseCase, DeleteUserUseCase deleteUserUseCase, UpdatePasswordUseCase updatePasswordUseCase,
                          AuthenticateUserUseCase authenticateUserUseCase, JwtTokenProvider jwtTokenProvider) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.updatePasswordUseCase = updatePasswordUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userRequest) {
        try {
            User user = authenticateUserUseCase.execute(userRequest.email(), userRequest.password());
            String token = jwtTokenProvider.generateToken(UserEntity.fromDomain(user));
            return ResponseEntity.ok(token);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_CREDENTIALS);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO dto) {
        User saved = createUserUseCase.execute(UserPresenter.fromDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserPresenter.toDTO(saved));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserDTO> createUserAdmin(@Valid @RequestBody UserDTO dto) {
        User saved = createUserUseCase.execute(UserPresenter.fromDTO(dto), UserRoles.ADMIN.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserPresenter.toDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable Long id) {
        return getUserUseCase.execute(id)
                .map(UserPresenter::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> list() {
        return ResponseEntity.ok(
                listUsersUseCase.execute().stream().map(UserPresenter::toDTO).toList()
        );
    }

    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO dto) {
        User saved = updateUserUseCase.execute(UserPresenter.fromDTO(dto));
        return ResponseEntity.ok(UserPresenter.toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestParam Long id, @RequestBody UpdatePasswordDTO request) {
        updatePasswordUseCase.execute(id, request.oldPassword(), request.newPassword());
        return ResponseEntity.ok(PASSWORD_UPDATED_SUCCESS);
    }
}
