package com.tokmakov.domain.strategy;

public interface ComputerMoveStrategy {
    /**
     * @return Возвращает ход {x, y}
     */
    int[] findMove(int[][] field);
}
