package com.example.techchallenge.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record UserRequest(

        Long id,
        @NotBlank(message = "Nome: é obrigatório")
        @Size(min = 2, max = 100, message = "Nome: deve ter entre 2 e 100 caracteres")
        String name,

        @NotBlank(message = "E-mail: é obrigatório")
        @Email(message = "E-mail: formato inválido")
        String email,

        @NotBlank(message = "Senha: é obrigatória")
        @Size(min = 6, message = "Senha: deve ter no mínimo 6 caracteres")
        String password,

        @NotNull(message = "Endereço: é obrigatório")
        @Valid
        AddressRequest address
) {
}
