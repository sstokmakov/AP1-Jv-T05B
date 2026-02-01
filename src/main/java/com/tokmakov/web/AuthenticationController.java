package com.tokmakov.web;

import com.tokmakov.domain.AuthenticationService;
import com.tokmakov.web.model.RefreshJwtRequest;
import com.tokmakov.domain.model.User;
import com.tokmakov.web.model.JwtRequest;
import com.tokmakov.web.model.JwtResponse;
import com.tokmakov.web.model.SignUpRequest;
import com.tokmakov.web.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/register")
    public boolean register(@RequestBody SignUpRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public JwtResponse authorize(@RequestBody JwtRequest request) {
        return authService.authorize(request);
    }

    @PostMapping("/refresh/access")
    public JwtResponse refreshAccessToken(@RequestBody RefreshJwtRequest request) {
        return authService.updateAccessToken(request.refreshToken());
    }

    @PostMapping("/refresh/refresh")
    public JwtResponse refreshRefreshToken(@RequestBody RefreshJwtRequest request) {
        return authService.updateRefreshToken(request.refreshToken());
    }

    @GetMapping("/me")
    public UserDto getMe() {
        User user = authService.getCurrentUser();
        return new UserDto(user.getUuid(), user.getLogin(), user.getRoles());
    }

    @GetMapping("/by-token")
    public UserDto getMeByAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        User user = authService.getUserByAccessToken(token);
        return new UserDto(user.getUuid(), user.getLogin(), user.getRoles());
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authorizationHeader.substring("Bearer ".length());
    }
}
