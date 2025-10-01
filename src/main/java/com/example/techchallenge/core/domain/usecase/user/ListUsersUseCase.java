package com.example.techchallenge.core.domain.usecase.user;

import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.interfaces.IUserGateway;

import java.util.List;

public class ListUsersUseCase {

    private final IUserGateway gateway;

    public ListUsersUseCase(IUserGateway gateway) {
        this.gateway = gateway;
    }

    public List<User> execute() {
        return gateway.findAll();
    }
}
