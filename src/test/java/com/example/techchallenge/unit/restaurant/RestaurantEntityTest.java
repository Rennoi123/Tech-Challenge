package com.example.techchallenge.unit.restaurant;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.core.domain.entities.Restaurant;
import com.example.techchallenge.infrastructure.entities.AddressEntity;
import com.example.techchallenge.infrastructure.entities.RestaurantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantEntityTest {

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address(
                1L, "Rua A", "123", "Ap 1", "Centro", "Cidade X", "SP", "12345-000"
        );
    }

    @Test
    void deveConverterDeDomainParaEntity() {
        Restaurant domain = new Restaurant(1L, "Restaurante A", address, "Italiana",
                LocalTime.of(10, 0), LocalTime.of(22, 0), null,100);

        RestaurantEntity entity = RestaurantEntity.fromDomain(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getName(), entity.getName());
        assertEquals(domain.getCuisineType(), entity.getCuisineType());
    }

    @Test
    void deveConverterDeEntityParaDomain() {
        AddressEntity addressEntity = new AddressEntity().fromDomain(address);

        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(1L);
        entity.setName("Restaurante B");
        entity.setCuisineType("Japonesa");
        entity.setOpeningTime(LocalTime.of(12, 0));
        entity.setClosingTime(LocalTime.of(23, 0));
        entity.setAddress(addressEntity);

        Restaurant domain = entity.toDomain();

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getName(), domain.getName());
        assertEquals(entity.getCuisineType(), domain.getCuisineType());
    }
}
