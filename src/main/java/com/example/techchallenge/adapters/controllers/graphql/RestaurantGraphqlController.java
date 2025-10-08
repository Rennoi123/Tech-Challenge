package com.example.techchallenge.adapters.controllers.graphql;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.domain.usecase.restaurant.CreateRestaurantUseCase;
import com.example.techchallenge.core.domain.usecase.restaurant.DeleteRestaurantUseCase;
import com.example.techchallenge.core.domain.usecase.restaurant.GetRestaurantByIdUseCase;
import com.example.techchallenge.core.domain.usecase.restaurant.ListRestaurantsUseCase;
import com.example.techchallenge.core.domain.usecase.restaurant.UpdateRestaurantUseCase;
import graphql.GraphQLError;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.util.List;

@Controller
public class RestaurantGraphqlController {

    private final ListRestaurantsUseCase listRestaurants;
    private final GetRestaurantByIdUseCase getRestaurantById;
    private final CreateRestaurantUseCase createRestaurant;
    private final UpdateRestaurantUseCase updateRestaurant;
    private final DeleteRestaurantUseCase deleteRestaurant;

    public RestaurantGraphqlController(
            ListRestaurantsUseCase listRestaurants,
            GetRestaurantByIdUseCase getRestaurantById,
            CreateRestaurantUseCase createRestaurant,
            UpdateRestaurantUseCase updateRestaurant,
            DeleteRestaurantUseCase deleteRestaurant
    ) {
        this.listRestaurants = listRestaurants;
        this.getRestaurantById = getRestaurantById;
        this.createRestaurant = createRestaurant;
        this.updateRestaurant = updateRestaurant;
        this.deleteRestaurant = deleteRestaurant;
    }

    @QueryMapping
    @PreAuthorize("permitAll()")
    public List<Restaurant> restaurants() {
        return listRestaurants.execute();
    }

    @QueryMapping
    @PreAuthorize("permitAll()")
    public Restaurant restaurantById(@Argument Long id) {
        return getRestaurantById.execute(id);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Restaurant createRestaurant(@Argument RestaurantInput input) {
        Restaurant restaurant = toEntity(input);
        return createRestaurant.execute(restaurant);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole(,'ADMIN')")
    public Restaurant updateRestaurant(@Argument Long id, @Argument RestaurantInput input) {
        Restaurant restaurant = toEntity(input);
        restaurant.setId(id);
        return updateRestaurant.execute(restaurant);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Boolean deleteRestaurant(@Argument Long id) {
        deleteRestaurant.execute(id);
        return true;
    }

    public record RestaurantInput(
            String name,
            String cuisineType,
            Long ownerId,
            String openingTime,
            String closingTime,
            AddressInput address,
            Long qtdTable
    ) {}

    public record AddressInput(
            Long id,
            String street,
            String city,
            String state,
            String postalCode,
            String number,
            String complement,
            String neighborhood
    ) {}

    private Restaurant toEntity(RestaurantInput input) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(input.name());
        restaurant.setCuisineType(input.cuisineType());
        restaurant.setOwnerId(input.ownerId());
        restaurant.setOpeningTime(LocalTime.parse(input.openingTime()));
        restaurant.setClosingTime(LocalTime.parse(input.closingTime()));
        restaurant.setQtdtable(input.qtdTable.intValue());
        restaurant.setCapacity(restaurant.getQtdtable() * 2);

        Address address = new Address();
        AddressInput addr = input.address();
        address.setId(addr.id);
        address.setStreet(addr.street());
        address.setCity(addr.city());
        address.setState(addr.state());
        address.setPostalCode(addr.postalCode());
        address.setNumber(addr.number());
        address.setComplement(addr.complement());
        address.setNeighborhood(addr.neighborhood);

        restaurant.setAddress(address);

        return restaurant;
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(Exception ex) {
        return GraphQLError.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .build();
    }
}
