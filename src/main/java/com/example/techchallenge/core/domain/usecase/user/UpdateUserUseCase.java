package com.example.techchallenge.core.domain.usecase.user;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.domain.usecase.address.CreateOrUpdateAddressUseCase;
import com.example.techchallenge.core.interfaces.IUserGateway;
import com.example.techchallenge.core.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UpdateUserUseCase {

    private static final String USER_NOT_FOUND_MESSAGE_BY_ID = "Usuário não encontrado pelo id: ";

    private final IUserGateway userGateway;
    private final CreateOrUpdateAddressUseCase addressUseCase;
    private final PasswordEncoder passwordEncoder;

    public UpdateUserUseCase(IUserGateway userGateway,
                             CreateOrUpdateAddressUseCase addressUseCase,
                             PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.addressUseCase = addressUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(User user) {
        user.validate();

        User existingUser = userGateway.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_ID + user.getId()));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

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

            existingUser.setAddress(addressUseCase.execute(address));
        }

        return userGateway.save(existingUser);
    }
}
