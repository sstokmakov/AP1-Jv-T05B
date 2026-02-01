package com.tokmakov.domain.util;

import com.tokmakov.exception.CellAlreadyOccupiedException;
import com.tokmakov.exception.CoordinatesOutOfBoundsException;
import com.tokmakov.domain.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameFieldValidator {
    public void validateTurn(Game game, int x, int y) {
        if (x < 0 || y < 0 || x >= GameUtils.FIELD_SIZE || y >= GameUtils.FIELD_SIZE)
            throw new CoordinatesOutOfBoundsException(x, y, GameUtils.FIELD_SIZE);
        if (game.getGameField()[y][x] != GameUtils.EMPTY_CELL)
            throw new CellAlreadyOccupiedException(x, y);
    }
}
