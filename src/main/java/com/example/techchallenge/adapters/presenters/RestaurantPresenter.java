package com.example.techchallenge.adapters.presenters;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.dto.AddressDTO;
import com.example.techchallenge.core.dto.RestaurantDTO;
import com.example.techchallenge.core.dto.RestaurantResponse;

public class RestaurantPresenter {

    public static RestaurantDTO toDTO(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getName(),
                new AddressDTO(
                        restaurant.getAddress().getId(),
                        restaurant.getAddress().getStreet(),
                        restaurant.getAddress().getNumber(),
                        restaurant.getAddress().getNeighborhood(),
                        restaurant.getAddress().getCity(),
                        restaurant.getAddress().getState(),
                        restaurant.getAddress().getPostalCode(),
                        restaurant.getAddress().getComplement()
                ),
                restaurant.getCuisineType(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime(),
                restaurant.getQtdtable()
        );
    }

    public static RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCuisineType(),
                restaurant.getOpeningTime(),
                restaurant.getClosingTime(),
                toDTO(restaurant).address(),
                restaurant.getCapacity(),
                restaurant.getQtdtable()

        );
    }

    public static Restaurant fromDTO(RestaurantDTO dto) {
        Address address = new Address(
                dto.address().id(),
                dto.address().street(),
                dto.address().number(),
                dto.address().complement(),
                dto.address().neighborhood(),
                dto.address().city(),
                dto.address().state(),
                dto.address().postalCode()
        );

        return new Restaurant(
                null,
                dto.name(),
                address,
                dto.cuisineType(),
                dto.openingTime(),
                dto.closingTime(),
                dto.qtdTable() * 2,
                dto.qtdTable()
                );
    }
}
