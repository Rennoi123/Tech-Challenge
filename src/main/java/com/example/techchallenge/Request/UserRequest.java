package com.example.techchallenge.Request;

import com.example.techchallenge.mapper.ModelMapperBase;
import com.example.techchallenge.model.UserEntity;

import java.util.Date;


public record UserRequest(
        Long id,
        String name,
        String email,
        String login,
        String password,
        Date lastModifiedDate,
        String address,
        boolean isActive
) {
    public UserEntity toEntity(){
        return ModelMapperBase.map(this, UserEntity.class);
    }

}
