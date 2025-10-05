package com.example.techchallenge.infrastructure.entities;

import com.example.techchallenge.core.domain.entities.Restaurant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "RESTAURANT")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @Column(nullable = false)
    private String cuisineType;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;

    @Positive
    @Column(nullable = false)
    private Integer capacity;

    @Positive
    @Column(nullable = false)
    private Integer qtdTable;

    public static RestaurantEntity fromDomain(Restaurant restaurant) {
        RestaurantEntity entity = new RestaurantEntity();
        entity.id = restaurant.getId();
        entity.name = restaurant.getName();
        entity.address = AddressEntity.fromDomain(restaurant.getAddress());
        entity.cuisineType = restaurant.getCuisineType();
        entity.openingTime = restaurant.getOpeningTime();
        entity.closingTime = restaurant.getClosingTime();
        entity.capacity = restaurant.getCapacity();
        entity.qtdTable = restaurant.getQtdtable();
        return entity;
    }

    public static RestaurantEntity fromDomain(Restaurant r, AddressEntity addressEntity, UserEntity ownerEntity) {
        RestaurantEntity e = new RestaurantEntity();
        e.id = r.getId();
        e.name = r.getName();
        e.address = addressEntity;
        e.cuisineType = r.getCuisineType();
        e.openingTime = r.getOpeningTime();
        e.closingTime = r.getClosingTime();
        e.owner = ownerEntity;
        e.capacity = r.getCapacity();
        e.qtdTable = r.getQtdtable();
        return e;
    }

    public Restaurant toDomain() {
        return new Restaurant(
                id,
                name,
                address != null ? address.toDomain() : null,
                cuisineType,
                openingTime,
                closingTime,
                owner != null ? owner.getId() : null,
                capacity,
                qtdTable
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AddressEntity getAddress() { return address; }
    public void setAddress(AddressEntity address) { this.address = address; }
    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }
    public LocalTime getOpeningTime() { return openingTime; }
    public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }
    public LocalTime getClosingTime() { return closingTime; }
    public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }
}