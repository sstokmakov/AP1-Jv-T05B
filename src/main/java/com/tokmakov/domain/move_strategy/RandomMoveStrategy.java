package com.tokmakov.domain.move_strategy;

import com.tokmakov.domain.util.GameUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomMoveStrategy implements MoveStrategy {
    @Override
    public int[] findMove(int[][] field) {
        Random random = new Random();
        return new int[]{random.nextInt(GameUtils.FIELD_SIZE), random.nextInt(GameUtils.FIELD_SIZE)};
    }
}
