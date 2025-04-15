package com.example.techchallenge.Request;

import com.example.techchallenge.mapper.ModelMapperBase;
import com.example.techchallenge.model.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        Long id,
        @NotBlank(message = "O nome é obrigatório")
        String name,
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,
        @NotBlank(message = "O usuário é obrigatório")
        String username,
        @NotBlank(message = "A senha é obrigatória")
        String password,
        String address
) {
    public UserEntity toEntity(){
        return ModelMapperBase.map(this, UserEntity.class);
    }

}
