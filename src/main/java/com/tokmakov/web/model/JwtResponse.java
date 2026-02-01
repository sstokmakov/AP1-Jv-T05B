package com.tokmakov.web.model;

public record JwtResponse(String type, String accessToken, String refreshToken) {
}
