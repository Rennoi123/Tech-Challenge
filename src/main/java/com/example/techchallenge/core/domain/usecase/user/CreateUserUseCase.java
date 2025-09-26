package com.example.techchallenge.core.domain.usecase.user;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.domain.usecase.address.CreateOrUpdateAddressUseCase;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.core.enums.UserRoles;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateUserUseCase {

    private final IUserGateway userGateway;
    private final CreateOrUpdateAddressUseCase addressUseCase;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(IUserGateway userGateway,
             CreateOrUpdateAddressUseCase addressUseCase,
             PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.addressUseCase = addressUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(User user) {
        return execute(user, UserRoles.CLIENTE.name());
    }

    public User execute(User user, String role) {
        user.validate();

        userGateway.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email já está em uso.");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role != null ? UserRoles.ADMIN : UserRoles.CLIENTE);

        if (user.getAddress() != null) {
            Address address = new Address(
                    user.getAddress().getId(),
                    user.getAddress().getStreet(),
                    user.getAddress().getNumber(),
                    user.getAddress().getComplement(),
                    user.getAddress().getNeighborhood(),
                    user.getAddress().getCity(),
                    user.getAddress().getState(),
                    user.getAddress().getPostalCode()
            );
            user.setAddress(addressUseCase.execute(address));
        }

        return userGateway.save(user);
    }
}
