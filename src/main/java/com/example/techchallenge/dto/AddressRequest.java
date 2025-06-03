package com.example.techchallenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        Long id,

        @NotBlank(message = "Rua: é obrigatória")
        @Size(max = 100, message = "Rua: deve ter no máximo 100 caracteres")
        String street,

        @NotBlank(message = "Número: é obrigatório")
        @Size(max = 10, message = "Número: deve ter no máximo 10 caracteres")
        String number,

        @Size(max = 50, message = "Complemento: deve ter no máximo 50 caracteres")
        String complement,

        @NotBlank(message = "Bairro: é obrigatório")
        @Size(max = 50, message = "Bairro: deve ter no máximo 50 caracteres")
        String neighborhood,

        @NotBlank(message = "Cidade: é obrigatória")
        @Size(max = 50, message = "Cidade: deve ter no máximo 50 caracteres")
        String city,

        @NotBlank(message = "Estado: é obrigatório")
        @Size(min = 2, max = 2, message = "Estado: deve ter 2 letras (ex: SP)")
        String state,

        @NotBlank(message = "CEP: é obrigatório")
        @Size(min = 8, max = 9, message = "CEP: deve ter entre 8 e 9 caracteres")
        String postalCode
) {}
