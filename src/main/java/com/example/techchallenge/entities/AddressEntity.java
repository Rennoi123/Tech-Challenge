package com.example.techchallenge.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.Date;

@Data
@Entity
@Table(name = "ADDRESS")
@Where(clause = "is_active = true")
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

    @Column(name = "is_active")
    private Boolean isActive = true;

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
}