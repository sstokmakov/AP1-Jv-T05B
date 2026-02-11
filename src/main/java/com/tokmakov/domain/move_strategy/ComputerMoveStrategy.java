package com.tokmakov.domain.move_strategy;

public interface ComputerMoveStrategy {
    /**
     * @return Возвращает ход {x, y}
     */
    int[] findMove(int[][] field);
}
