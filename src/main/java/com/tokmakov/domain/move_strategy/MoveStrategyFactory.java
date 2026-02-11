package com.tokmakov.domain.move_strategy;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MoveStrategyFactory {
    private final Map<String, MoveStrategy> strategies;

    public MoveStrategyFactory(Map<String, MoveStrategy> strategies) {
        this.strategies = strategies;
    }

    public MoveStrategy getRequired(String name) {
        MoveStrategy strategy = strategies.get(name);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown move strategy: " + name +
                    ". Available: " + strategies.keySet());
        }
        return strategy;
    }
}
