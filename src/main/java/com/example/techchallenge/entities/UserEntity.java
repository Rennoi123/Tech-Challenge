package com.example.techchallenge.entities;

import com.example.techchallenge.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "TB_USERS")
public class UserEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private Date lastModifiedDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    private UserRoles roles;

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
