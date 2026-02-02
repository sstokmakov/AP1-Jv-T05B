package com.tokmakov.domain;

import com.tokmakov.domain.model.Game;
import com.tokmakov.domain.model.WinRatio;

import java.util.List;
import java.util.UUID;

public interface GameService {
    Game createGameWithPlayer(String playerUuid);

    Game createGameWithComputer(String playerUuid);

    Game joinGame(String gameUuid, String playerUuid);

    List<String> availableGames(String userUuid);

    Game gameByUuid(String uuid);

    Game processTurn(String gameUuid, String playerUuid, int x, int y);

    boolean isGameFinished(Game game);

    List<Game> completedGamesByUserUuid(UUID userUuid);

    List<WinRatio> topPlayers(int limit);
}
