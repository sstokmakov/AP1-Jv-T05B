package com.tokmakov.domain;

import com.tokmakov.domain.model.Role;
import com.tokmakov.domain.model.User;
import com.tokmakov.web.model.JwtRequest;
import com.tokmakov.web.model.JwtResponse;
import com.tokmakov.web.model.SignUpRequest;
import com.tokmakov.security.jwt.JwtAuthentication;
import com.tokmakov.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public boolean register(SignUpRequest request) {
        if (!isCorrectSignUpRequest(request)) throw new IllegalArgumentException("Login and password must be provided");
        if (userService.isUserExist(request.username())) return false;

        String encodedPassword = passwordEncoder.encode(request.password());
        List<Role> roles = List.of(Role.USER);
        User user = new User(
                UUID.randomUUID(),
                request.username(),
                encodedPassword,
                roles);
        userService.save(user);
        return true;
    }

    private boolean isCorrectSignUpRequest(SignUpRequest request) {
        return !(request == null || request.username().isBlank() || request.password().isBlank());
    }

    public JwtResponse authorize(JwtRequest request) {
        if (request == null || request.login().isBlank() || request.password().isBlank()) {
            throw new IllegalArgumentException("Login and password must be provided");
        }
        User user = userService.findByLogin(request.login());
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid login or password");
        }
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        return new JwtResponse("Bearer", accessToken, refreshToken);
    }

    public JwtResponse updateAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank() || !jwtProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        UUID userUuid = extractUuid(jwtProvider.getClaims(refreshToken).get("uuid"));
        User user = userService.findByUuid(userUuid);
        String accessToken = jwtProvider.generateAccessToken(user);
        return new JwtResponse("Bearer", accessToken, refreshToken);
    }

    public JwtResponse updateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank() || !jwtProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        UUID userUuid = extractUuid(jwtProvider.getClaims(refreshToken).get("uuid"));
        User user = userService.findByUuid(userUuid);
        String accessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);
        return new JwtResponse("Bearer", accessToken, newRefreshToken);
    }

    public User getUserByAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank() || !jwtProvider.validateAccessToken(accessToken)) {
            throw new IllegalArgumentException("Invalid access token");
        }
        UUID uuid = extractUuid(jwtProvider.getClaims(accessToken).get("uuid"));
        return userService.findByUuid(uuid);
    }

    public User getCurrentUser() {
        JwtAuthentication authentication = getJwtAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new IllegalStateException("User is not authenticated");
        }
        return userService.findByUuid(extractUuid(principal));
    }

    private JwtAuthentication getJwtAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication jwtAuthentication) {
            return jwtAuthentication;
        }
        return new JwtAuthentication(null, List.of(), false);
    }

    private UUID extractUuid(Object value) {
        if (value instanceof UUID uuid) {
            return uuid;
        }
        if (value instanceof String text && !text.isBlank()) {
            return UUID.fromString(text);
        }
        throw new IllegalArgumentException("Invalid token subject");
    }
}
