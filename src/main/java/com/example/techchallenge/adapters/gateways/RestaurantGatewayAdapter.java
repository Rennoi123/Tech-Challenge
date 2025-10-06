package com.example.techchallenge.adapters.gateways;

import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.core.interfaces.IRestaurantGateway;
import com.example.techchallenge.infrastructure.entities.AddressEntity;
import com.example.techchallenge.infrastructure.entities.RestaurantEntity;
import com.example.techchallenge.infrastructure.entities.UserEntity;
import com.example.techchallenge.infrastructure.repository.AddressRepository;
import com.example.techchallenge.infrastructure.repository.RestaurantRepository;
import com.example.techchallenge.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantGatewayAdapter implements IRestaurantGateway {

    private final String OWNER_NOT_FOUND_MSG = "Dono (user) não encontrado: ";

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public RestaurantGatewayAdapter(RestaurantRepository restaurantRepository,
                                    AddressRepository addressRepository,
                                    UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        AddressEntity addressEntity = null;

        if (restaurant.getAddress() != null) {
            if (restaurant.getAddress().getId() != null) {
                addressEntity = addressRepository.findById(restaurant.getAddress().getId())
                        .orElseGet(() -> AddressEntity.fromDomain(restaurant.getAddress()));
            } else {
                addressEntity = AddressEntity.fromDomain(restaurant.getAddress());
            }
        }

        UserEntity ownerEntity = userRepository.findById(restaurant.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException(OWNER_NOT_FOUND_MSG + restaurant.getOwnerId()));

        RestaurantEntity toPersist = RestaurantEntity.fromDomain(restaurant, addressEntity, ownerEntity);
        return restaurantRepository.save(toPersist).toDomain();
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id).map(RestaurantEntity::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll().stream().map(RestaurantEntity::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return restaurantRepository.existsById(id);
    }

    @Override
    public long countByAddressId(Long addressId) {
        return restaurantRepository.countByAddressId(addressId);
    }

    @Override
    public List<Restaurant> findByOwnerId(Long userId) {
        return restaurantRepository.findByOwnerId(userId).stream().map(RestaurantEntity::toDomain).toList();
    }
}
