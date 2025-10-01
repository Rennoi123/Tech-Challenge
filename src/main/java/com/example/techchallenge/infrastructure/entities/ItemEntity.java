package com.example.techchallenge.infrastructure.entities;

import com.example.techchallenge.core.domain.entities.Item;
import com.example.techchallenge.infrastructure.entities.RestaurantEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_ITEMS")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean dineInOnly;

    private String photoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    public static ItemEntity fromDomain(Item item, RestaurantEntity restaurantEntity) {
        ItemEntity entity = new ItemEntity();
        entity.id = item.getId();
        entity.name = item.getName();
        entity.description = item.getDescription();
        entity.price = item.getPrice();
        entity.dineInOnly = item.isDineInOnly();
        entity.photoPath = item.getPhotoPath();
        entity.restaurant = restaurantEntity;
        return entity;
    }

    public Item toDomain() {
        return new Item(id, name, description, price, dineInOnly, photoPath,
                restaurant != null ? restaurant.getId() : null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isDineInOnly() { return dineInOnly; }
    public void setDineInOnly(boolean dineInOnly) { this.dineInOnly = dineInOnly; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public RestaurantEntity getRestaurant() { return restaurant; }
    public void setRestaurant(RestaurantEntity restaurant) { this.restaurant = restaurant; }
}
