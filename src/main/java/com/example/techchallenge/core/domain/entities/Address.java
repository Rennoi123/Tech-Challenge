package com.example.techchallenge.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {

    private static final String STREET_REQUIRED = "Rua é obrigatória";
    private static final String CITY_REQUIRED = "Cidade é obrigatória";
    private static final String STATE_REQUIRED = "Estado é obrigatório";
    private static final String POSTAL_CODE_REQUIRED = "CEP/ZipCode é obrigatório";

    private Long id;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String postalCode;

    public void validate() {
        validateField(street, STREET_REQUIRED);
        validateField(city, CITY_REQUIRED);
        validateField(state, STATE_REQUIRED);
        validateField(postalCode, POSTAL_CODE_REQUIRED);
    }

    private void validateField(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
