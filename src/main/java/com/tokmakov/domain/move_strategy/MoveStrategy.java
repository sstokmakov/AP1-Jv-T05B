package com.tokmakov.domain.move_strategy;

public interface MoveStrategy {
    /**
     * @return Возвращает ход {x, y}
     */
    int[] findMove(int[][] field);
}
