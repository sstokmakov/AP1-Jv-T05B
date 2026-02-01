package com.tokmakov.web.model;

import com.tokmakov.domain.model.Role;

import java.util.List;
import java.util.UUID;

public record UserDto(UUID uuid, String login, List<Role> roles) {
}
