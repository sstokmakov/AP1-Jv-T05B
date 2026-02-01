package com.tokmakov.datasource.mapper;

import com.tokmakov.domain.model.User;
import com.tokmakov.datasource.entity.UserEntity;

import java.util.List;

public class UserEntityMapper {
    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setUuid(user.getUuid());
        entity.setLogin(user.getLogin());
        entity.setPassword(user.getPassword());
        entity.setRoles(user.getRoles() == null ? List.of() : user.getRoles());
        return entity;
    }

    public static User toUser(UserEntity entity) {
        return new User(entity.getUuid(),
                entity.getLogin(),
                entity.getPassword(),
                entity.getRoles() == null ? List.of() : entity.getRoles());
    }
}
