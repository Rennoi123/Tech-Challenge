package com.example.techchallenge.infrastructure.configuration;

import com.example.techchallenge.core.domain.usecase.address.CreateOrUpdateAddressUseCase;
import com.example.techchallenge.core.domain.usecase.user.*;
import com.example.techchallenge.core.interfaces.IUserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase( IUserGateway userGateway,
                                                CreateOrUpdateAddressUseCase addressUseCase,
                                                PasswordEncoder passwordEncoder) {
        return new CreateUserUseCase(userGateway, addressUseCase, passwordEncoder);
    }

    @Bean
    public GetUserUseCase getUserUseCase(IUserGateway userGateway) {
        return new GetUserUseCase(userGateway);
    }

    @Bean
    public ListUsersUseCase listUsersUseCase(IUserGateway userGateway) {
        return new ListUsersUseCase(userGateway);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase( IUserGateway userGateway,
                                                CreateOrUpdateAddressUseCase addressUseCase,
                                                PasswordEncoder passwordEncoder) {
        return new UpdateUserUseCase(userGateway, addressUseCase, passwordEncoder);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(IUserGateway userGateway) {
        return new DeleteUserUseCase(userGateway);
    }

    @Bean
    public UpdatePasswordUseCase updatePasswordUseCase(IUserGateway userGateway, PasswordEncoder passwordEncoder) {
        return new UpdatePasswordUseCase(userGateway, passwordEncoder);
    }

    @Bean
    public ValidateCredentialsUseCase validateCredentialsUseCase(IUserGateway userGateway, PasswordEncoder passwordEncoder) {
        return new ValidateCredentialsUseCase(userGateway, passwordEncoder);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(IUserGateway userGateway, PasswordEncoder passwordEncoder) {
        return new AuthenticateUserUseCase(userGateway, passwordEncoder);
    }
}