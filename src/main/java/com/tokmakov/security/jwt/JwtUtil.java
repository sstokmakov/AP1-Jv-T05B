package com.tokmakov.security.jwt;

import com.tokmakov.domain.model.Role;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class JwtUtil {
    public JwtAuthentication createAuthentication(Claims claims) {
        String uuid = claims.get("uuid", String.class);
        if (uuid == null || uuid.isBlank()) {
            return new JwtAuthentication(null, List.of(), false);
        }
        List<Role> roles = extractRoles(claims.get("roles"));
        return new JwtAuthentication(uuid, roles, true);
    }

    private List<Role> extractRoles(Object rolesClaim) {
        if (!(rolesClaim instanceof List<?> rolesList)) {
            return List.of();
        }
        return rolesList.stream()
                .filter(Objects::nonNull)
                .map(role -> Role.valueOf(role.toString()))
                .toList();
    }
}
