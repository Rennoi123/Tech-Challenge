package com.example.techchallenge.model;

import com.example.techchallenge.Enum.UserRoles;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users")
@Where(clause = "is_active = true")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private Boolean isActive;
    private Date lastModifiedDate;
    private String address;
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