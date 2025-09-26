package com.example.techchallenge.adapters.presenters;

import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.dto.UserDTO;

public class UserPresenter {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            AddressPresenter.toDTO(user.getAddress()),
            user.getRole()
        );
    }

    public static User fromDTO(UserDTO dto) {
        return new User(
            dto.id(),
            dto.name(),
            dto.email(),
            dto.password(),
            dto.userRoles(),
            AddressPresenter.fromDTO(dto.address())
        );
    }
}
