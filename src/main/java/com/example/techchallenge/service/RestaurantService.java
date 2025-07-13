package com.example.techchallenge.service;

import com.example.techchallenge.dto.Response.AddressResponse;
import com.example.techchallenge.dto.Request.RestaurantRequest;
import com.example.techchallenge.dto.Response.RestaurantResponse;
import com.example.techchallenge.entities.AddressEntity;
import com.example.techchallenge.entities.RestaurantEntity;
import com.example.techchallenge.entities.UserEntity;
import com.example.techchallenge.enums.UserRoles;
import com.example.techchallenge.exception.UserNotFoundException;
import com.example.techchallenge.repository.RestaurantRepository;
import com.example.techchallenge.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private static final String RESTAURANT_NOT_FOUND_MESSAGE_BY_ID = "Restaurante não encontrado pelo id: ";

    private static final String VALID_MESSAGE_OWNER_RESTAURANT = "Usuário  sem permissão para criar um Restaurante ";
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final AddressService addressService;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, AddressService addressService) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.addressService = addressService;
    }

    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        UserEntity owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new UserNotFoundException("Dono do restaurante não encontrado pelo ID: " + request.ownerId()));

        if (owner.getRoles() != UserRoles.RESTAURANTE) {
            throw new IllegalArgumentException(VALID_MESSAGE_OWNER_RESTAURANT);
        }

        AddressEntity address = addressService.createOrUpdateAddress(request.address());

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setName(request.name());
        restaurant.setAddress(address);
        restaurant.setCuisineType(request.cuisineType());
        restaurant.setOpeningTime(request.openingTime());
        restaurant.setClosingTime(request.closingTime());
        restaurant.setOwner(owner);

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);
        return toResponse(savedRestaurant);
    }

    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RestaurantResponse getRestaurantById(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + id));
        return toResponse(restaurant);
    }

    public void deleteRestaurant(Long id){
        if (!restaurantRepository.existsById(id)) {
            throw new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + id);
        }
        restaurantRepository.deleteById(id);
    }


    private RestaurantResponse toResponse(RestaurantEntity entity) {
        return new RestaurantResponse(
                entity.getId(),
                entity.getName(),
                entity.getCuisineType(),
                entity.getOpeningTime(),
                entity.getClosingTime(),
                entity.getOwner().getId(),
                new AddressResponse(
                        entity.getAddress().getId(),
                        entity.getAddress().getStreet(),
                        entity.getAddress().getNumber(),
                        entity.getAddress().getComplement(),
                        entity.getAddress().getNeighborhood(),
                        entity.getAddress().getCity(),
                        entity.getAddress().getState(),
                        entity.getAddress().getPostalCode()
                )

        );
    }
}