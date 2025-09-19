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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private static final String RESTAURANT_NOT_FOUND_MESSAGE_BY_ID = "Restaurante não encontrado pelo id: ";
    private static final String VALID_MESSAGE_OWNER_RESTAURANT = "Usuário  sem permissão para criar um Restaurante ";
    private static final String VALID_MESSAGE_ADDRESS_RESTAURANT = "Endereço já está associado a outro restaurante.";
    private static final String USER_NOT_FOUND_MESSAGE_BY_ID = "Dono do restaurante não encontrado pelo ID: ";
    public static final String USER_NOT_FOUND_MESSAGE_BY_EMAIL = "Usuário não encontrado com o email: ";

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final AddressService addressService;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, AddressService addressService) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.addressService = addressService;
    }

    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE_BY_EMAIL + email));

        int total = restaurantRepository.countByAddressId(request.address().id());

        if (total > 0) {
            throw new IllegalArgumentException(VALID_MESSAGE_ADDRESS_RESTAURANT);
        }

        if (!user.getRoles().equals(UserRoles.ADMIN)) {
            throw new IllegalArgumentException(VALID_MESSAGE_OWNER_RESTAURANT);
        }

        AddressEntity address = addressService.createOrUpdateAddress(request.address());

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setName(request.name());
        restaurant.setAddress(address);
        restaurant.setCuisineType(request.cuisineType());
        restaurant.setOpeningTime(request.openingTime());
        restaurant.setClosingTime(request.closingTime());
        restaurant.setOwner(user);

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


    public void deleteRestaurant(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND_MESSAGE_BY_ID + id));

        restaurantRepository.delete(restaurant);
    }

    private RestaurantResponse toResponse(RestaurantEntity entity) {
        return new RestaurantResponse(
                entity.getId(),
                entity.getName(),
                entity.getCuisineType(),
                entity.getOpeningTime(),
                entity.getClosingTime(),

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