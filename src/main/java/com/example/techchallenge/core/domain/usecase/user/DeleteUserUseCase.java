package com.example.techchallenge.core.domain.usecase.user;

import com.example.techchallenge.core.interfaces.IUserGateway;

public class DeleteUserUseCase {

    private final IUserGateway gateway;

    public DeleteUserUseCase(IUserGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(Long id) {
        gateway.deleteById(id);
    }
}
