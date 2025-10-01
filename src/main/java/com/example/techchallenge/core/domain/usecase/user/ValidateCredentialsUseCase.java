package com.example.techchallenge.core.domain.usecase.user;

import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.core.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ValidateCredentialsUseCase {

    private static final String INVALID_EMAIL_MESSAGE = "Email não encontrado.";
    private static final String INVALID_PASSWORD_MESSAGE = "Senha inválida para o email informado.";

    private final IUserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public ValidateCredentialsUseCase(IUserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean execute(String email, String rawPassword) {
        User user = userGateway.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_EMAIL_MESSAGE));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException(INVALID_PASSWORD_MESSAGE);
        }

        return true;
    }
}
