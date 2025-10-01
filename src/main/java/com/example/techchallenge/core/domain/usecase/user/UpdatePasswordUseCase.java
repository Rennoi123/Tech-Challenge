package com.example.techchallenge.core.domain.usecase.user;

import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.core.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UpdatePasswordUseCase {

    private static final String USER_NOT_FOUND_MESSAGE_BY_ID = "Usuário não encontrado pelo id: ";
    private static final String EMPTY_PASSWORD_MESSAGE = "Senha não pode ser vazia.";
    private static final String INVALID_PASSWORD_ATUAL = "Senha atual incorreta.";

    private final IUserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public UpdatePasswordUseCase(IUserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(Long userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException(EMPTY_PASSWORD_MESSAGE);
        }

        User user = userGateway.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException(USER_NOT_FOUND_MESSAGE_BY_ID + userId));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidCredentialsException(INVALID_PASSWORD_ATUAL);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userGateway.save(user);
    }
}
