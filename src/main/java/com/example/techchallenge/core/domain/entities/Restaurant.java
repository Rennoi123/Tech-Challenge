package com.example.techchallenge.core.domain.entities;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Restaurant {
    private static final String NOME_OBRIGATORIO = "Nome é obrigatório";
    private static final String ENDERECO_OBRIGATORIO = "Endereço é obrigatório";
    private static final String TIPO_COZINHA_OBRIGATORIO = "Tipo de cozinha é obrigatório";
    private static final String HORARIO_OBRIGATORIO = "Horário de funcionamento é obrigatório";
    private static final String CAPACIDADE_MAXIMA = "Capacidade máxima é obrigatória";
    private static final String QUANTIDADE_MESA = "Quantidade de mesa precisa ser maior que 0 e é obrigatória";

    private Long id;
    private String name;
    private Address address;
    private String cuisineType;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Long ownerId;
    private Integer capacity;
    private Integer qtdtable;

    public Restaurant(Long id, String name, Address address, String cuisineType,
                      LocalTime openingTime, LocalTime closingTime, Integer capacity, Integer qtdtable) {
        this(id, name, address, cuisineType, openingTime, closingTime, null, capacity, qtdtable);
    }

    public Restaurant(Long id, String name, Address address, String cuisineType,
                      LocalTime openingTime, LocalTime closingTime, Long ownerId, Integer capacity, Integer qtdtable) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cuisineType = cuisineType;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.ownerId = ownerId;
        this.capacity = capacity;
        this.qtdtable = qtdtable;
        validate();
    }

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(NOME_OBRIGATORIO);
        }

        if (address == null) {
            throw new IllegalArgumentException(ENDERECO_OBRIGATORIO);
        }

        if (cuisineType == null || cuisineType.isBlank()) {
            throw new IllegalArgumentException(TIPO_COZINHA_OBRIGATORIO);
        }

        if (openingTime == null || closingTime == null) {
            throw new IllegalArgumentException(HORARIO_OBRIGATORIO);
        }
        if(capacity == null || capacity <= 0){
            throw new IllegalArgumentException(CAPACIDADE_MAXIMA);
        }
        if(qtdtable == null || qtdtable <= 0){
            throw new IllegalArgumentException(QUANTIDADE_MESA);
        }
    }
}
