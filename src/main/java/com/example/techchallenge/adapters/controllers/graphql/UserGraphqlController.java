package com.example.techchallenge.adapters.controllers.graphql;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.User;
import com.example.techchallenge.core.domain.usecase.user.CreateUserUseCase;
import com.example.techchallenge.core.domain.usecase.user.GetUserUseCase;
import com.example.techchallenge.core.domain.usecase.user.ListUsersUseCase;
import com.example.techchallenge.core.domain.usecase.user.UpdateUserUseCase;
import com.example.techchallenge.core.domain.usecase.user.ValidateCredentialsUseCase;
import com.example.techchallenge.core.enums.UserRoles;
import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserGraphqlController {

    private final ListUsersUseCase listUsersUseCase;
    private final GetUserUseCase getUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    public UserGraphqlController(
            ListUsersUseCase listUsersUseCase,
            GetUserUseCase getUserUseCase,
            CreateUserUseCase createUserUseCase,
            UpdateUserUseCase updateUserUseCase
    ) {
        this.listUsersUseCase = listUsersUseCase;
        this.getUserUseCase = getUserUseCase;
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> users() {
        return listUsersUseCase.execute();
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public User userById(@Argument Long id) {
        return getUserUseCase.execute(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
    }

    @MutationMapping
    @PreAuthorize("permitAll()")
    public User createUser(
            @Argument String name,
            @Argument String email,
            @Argument String password,
            @Argument UserRoles role,
            @Argument AddressInput address
    ) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        Address addr = new Address();
        addr.setId(address.id);
        addr.setStreet(address.street());
        addr.setCity(address.city());
        addr.setState(address.state());
        addr.setPostalCode(address.postalCode());
        addr.setNumber(address.number());
        addr.setComplement(address.complement());
        addr.setNeighborhood(address.Neighborhood);
        user.setAddress(addr);

        if (role != null) {
            user.setRole(role);
        } else {
            user.setRole(UserRoles.CLIENTE);
        }

        return createUserUseCase.execute(user);
    }


    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public User updateUser(
            @Argument Long id,
            @Argument String name,
            @Argument String email,
            @Argument String password,
            @Argument UserRoles role,
            @Argument AddressInput address
    ) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        Address addr = new Address();
        addr.setId(address.id);
        addr.setStreet(address.street());
        addr.setCity(address.city());
        addr.setState(address.state());
        addr.setPostalCode(address.postalCode());
        addr.setNeighborhood(address.Neighborhood);
        addr.setNumber(address.number());
        addr.setComplement(address.complement());
        user.setAddress(addr);

        if (role != null ) {
            user.setRole(role);
        }

        return updateUserUseCase.execute(user);
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(Exception ex) {
        return GraphQLError.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .build();
    }

    public record AddressInput(
            Long id,
            String street,
            String city,
            String state,
            String postalCode,
            String number,
            String complement,
            String Neighborhood
    ) {}

}
