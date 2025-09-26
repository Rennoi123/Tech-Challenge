package com.example.techchallenge.core.domain.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Item {

    public static final String MSG_NOME_OBRIGATORIO = "Nome do item é obrigatório";
    public static final String MSG_PRECO_POSITIVO = "Preço deve ser positivo";
    public static final String MSG_RESTAURANTE_OBRIGATORIO = "Restaurante é obrigatório";

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean dineInOnly;
    private String photoPath;
    private Long restaurantId;

    public Item(Long id, String name, String description, BigDecimal price,
                boolean dineInOnly, String photoPath, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.photoPath = photoPath;
        this.restaurantId = restaurantId;
        validate();
    }

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(MSG_NOME_OBRIGATORIO);
        }

        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException(MSG_PRECO_POSITIVO);
        }

        if (restaurantId == null) {
            throw new IllegalArgumentException(MSG_RESTAURANTE_OBRIGATORIO);
        }
    }
}
