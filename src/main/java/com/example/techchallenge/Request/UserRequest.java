package com.example.techchallenge.Request;

import com.example.techchallenge.mapper.ModelMapperBase;
import com.example.techchallenge.model.UserEntity;
import com.sun.istack.NotNull;
import jakarta.validation.constraints.Email;


public record UserRequest(
        Long id,

        @NotNull
        String name,
        @NotNull
        @Email
        String email,
        @NotNull
        String username,
        @NotNull
        String password,
        @NotNull
        String address
) {
    public UserEntity toEntity(){
        return ModelMapperBase.map(this, UserEntity.class);
    }

}
