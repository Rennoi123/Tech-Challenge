package com.example.techchallenge.core.domain.entities;

import com.example.techchallenge.core.enums.UserRoles;
import lombok.Data;

import java.util.Date;

@Data
public class User {

    private static final String NOME_OBRIGATORIO = "Nome é obrigatório";
    private static final String EMAIL_INVALIDO = "E-mail inválido";

    private Long id;
    private String name;
    private String email;
    private String password;
    private Date createdDate;
    private Date lastModifiedDate;
    private UserRoles role;
    private Address address; // referência adicionada

    public User() {
        this.createdDate = new Date();
        this.lastModifiedDate = new Date();
    }

    public User(Long id, String name, String email, String password, UserRoles role, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = address;
        this.createdDate = new Date();
        this.lastModifiedDate = new Date();
        validate();
    }

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(NOME_OBRIGATORIO);
        }

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException(EMAIL_INVALIDO);
        }
    }
}
