package com.example.techchallenge.adapters.controllers.graphql;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.usecase.address.CreateOrUpdateAddressUseCase;
import com.example.techchallenge.core.domain.usecase.address.DeleteAddressUseCase;
import com.example.techchallenge.core.domain.usecase.address.GetAddressByIdUseCase;
import com.example.techchallenge.core.domain.usecase.address.ListAddressesUseCase;
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
public class AddressGraphqlController {

    private final ListAddressesUseCase listAddresses;
    private final GetAddressByIdUseCase getAddressById;
    private final CreateOrUpdateAddressUseCase createOrUpdateAddress;
    private final DeleteAddressUseCase deleteAddress;

    public AddressGraphqlController(ListAddressesUseCase listAddresses,
                                    GetAddressByIdUseCase getAddressById,
                                    CreateOrUpdateAddressUseCase createOrUpdateAddress,
                                    DeleteAddressUseCase deleteAddress) {
        this.listAddresses = listAddresses;
        this.getAddressById = getAddressById;
        this.createOrUpdateAddress = createOrUpdateAddress;
        this.deleteAddress = deleteAddress;
    }

    @QueryMapping
    public List<Address> addresses() {
        return listAddresses.execute();
    }

    @QueryMapping
    public Address addressById(@Argument Long id) {
        return getAddressById.execute(id);
    }

    @MutationMapping
    public Address upsertAddress(@Argument AddressInput input) {
        Address a = new Address(
                input.id(),
                input.street(),
                input.number(),
                input.complement(),
                input.neighborhood(),
                input.city(),
                input.state(),
                input.postalCode()
        );
        return createOrUpdateAddress.execute(a);
    }

    @MutationMapping
    public Boolean deleteAddress(@Argument Long id) {
        deleteAddress.execute(id);
        return true;
    }

    public record AddressInput(
            Long id,
            String street,
            String number,
            String complement,
            String neighborhood,
            String city,
            String state,
            String postalCode
    ) {}

    @GraphQlExceptionHandler
    public GraphQLError handle(Exception ex) {
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage()).build();
    }
}
