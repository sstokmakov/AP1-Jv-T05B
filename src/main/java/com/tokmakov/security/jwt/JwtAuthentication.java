package com.tokmakov.security.jwt;

import com.tokmakov.domain.model.Role;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class JwtAuthentication implements Authentication {
    private final String uuid;
    private final List<Role> roles;
    private boolean authenticated;

    public JwtAuthentication(String uuid, List<Role> roles, boolean authenticated) {
        this.uuid = uuid;
        this.roles = roles;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getDetails() {
        return null;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return uuid;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return uuid;
    }
}
