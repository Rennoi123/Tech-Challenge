package com.example.techchallenge.infrastructure.entities;

import com.example.techchallenge.core.domain.entities.Address;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "number", nullable = false, length = 10)
    private String number;

    @Column(name = "complement", length = 50)
    private String complement;

    @Column(name = "neighborhood", nullable = false, length = 50)
    private String neighborhood;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "state", nullable = false, length = 2)
    private String state;

    @Column(name = "postal_code", nullable = false, length = 9)
    private String postalCode;

    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = new Date();
    }

    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
        lastModifiedDate = new Date();
    }

    public static AddressEntity fromDomain(Address address) {
        AddressEntity entity = new AddressEntity();
        entity.id = address.getId();
        entity.street = address.getStreet();
        entity.number = address.getNumber();
        entity.neighborhood = address.getNeighborhood();
        entity.city = address.getCity();
        entity.state = address.getState();
        entity.postalCode = address.getPostalCode();
        entity.complement = address.getComplement();
        return entity;
    }

    public Address toDomain() {
        return new Address(id, street, number, neighborhood, city, state, postalCode, complement);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getComplement() { return complement; }
    public void setComplement(String complement) { this.complement = complement; }
}