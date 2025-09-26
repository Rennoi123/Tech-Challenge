package com.example.techchallenge.core.domain.usecase.user;

import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.exception.InvalidCredentialsException;
import com.example.techchallenge.core.interfaces.IUserGateway;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticateUserUseCase {

    private final static String INVALID_PASSWORD_MESSAGE = "Senha inválida para o email informado.";
    private final static String EMAIL_NOT_FOUND_MESSAGE = "Email não encontrado.";

    private final IUserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateUserUseCase(IUserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(String email, String rawPassword) {
        User user = userGateway.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(EMAIL_NOT_FOUND_MESSAGE));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException(INVALID_PASSWORD_MESSAGE);
        }

        return user;
    }
}
