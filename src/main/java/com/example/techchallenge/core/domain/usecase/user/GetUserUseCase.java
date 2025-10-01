package com.example.techchallenge.core.domain.usecase.user;

import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.interfaces.IUserGateway;

import java.util.Optional;

public class GetUserUseCase {

    private final IUserGateway gateway;

    public GetUserUseCase(IUserGateway gateway) {
        this.gateway = gateway;
    }

    public Optional<User> execute(Long id) {
        return gateway.findById(id);
    }
}
