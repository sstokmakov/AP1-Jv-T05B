package com.tokmakov.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID uuid;
    private String login;
    private String password;
    private List<Role> roles;

    public User(UUID uuid, String login, String password, List<Role> roles) {
        this.uuid = uuid;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }
}
