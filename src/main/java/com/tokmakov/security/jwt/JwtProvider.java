package com.tokmakov.security.jwt;

import com.tokmakov.domain.model.Role;
import com.tokmakov.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtProvider {
    private final Key key;
    private final long accessTokenTtlMs;
    private final long refreshTokenTtlMs;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-ttl-ms}") long accessTokenTtlMs,
            @Value("${jwt.refresh-ttl-ms}") long refreshTokenTtlMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenTtlMs = accessTokenTtlMs;
        this.refreshTokenTtlMs = refreshTokenTtlMs;
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid().toString());
        claims.put("roles", rolesToStrings(user.getRoles()));
        return generateToken(claims, accessTokenTtlMs);
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid().toString());
        return generateToken(claims, refreshTokenTtlMs);
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token);
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String generateToken(Map<String, Object> claims, long ttlMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ttlMs);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private List<String> rolesToStrings(List<Role> roles) {
        if (roles == null) return List.of();
        return roles.stream().map(Role::name).toList();
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
