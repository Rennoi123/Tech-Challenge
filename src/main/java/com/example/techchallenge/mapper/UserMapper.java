package com.example.techchallenge.mapper;

import com.example.techchallenge.model.UserEntity;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BaseMapper<UserEntity> {
    @Override
    public UserEntity create(UserEntity source) {
        return ModelMapperBase.map(source, UserEntity.class);
    }

    @Override
    public void update(UserEntity source, UserEntity destination) {
    }
}