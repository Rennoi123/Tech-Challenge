package com.example.techchallenge.infrastructure.entities;

import com.example.techchallenge.core.enums.UserRoles;
import com.example.techchallenge.core.domain.entities.User;
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

    @OneToOne(fetch = FetchType.EAGER)
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

    public static UserEntity fromDomain(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = user.getId();
        userEntity.name = user.getName();
        userEntity.email = user.getEmail();
        userEntity.password = user.getPassword();
        userEntity.createdDate = user.getCreatedDate();
        userEntity.lastModifiedDate = user.getLastModifiedDate();
        userEntity.roles = user.getRole();
        userEntity.address = user.getAddress() != null ? AddressEntity.fromDomain(user.getAddress()) : null;
        return userEntity;
    }

    public User toDomain() {
        return new User(id, name, email, password, roles, address.toDomain());
    }
}
