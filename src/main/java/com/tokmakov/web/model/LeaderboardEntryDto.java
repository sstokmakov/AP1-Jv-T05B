package com.tokmakov.web.model;

import java.util.UUID;

public record LeaderboardEntryDto(UUID userUuid, String login, double winRatio) {
}
