package com.tokmakov.datasource;

import com.tokmakov.domain.model.GameStatus;
import com.tokmakov.datasource.entity.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, UUID> {
    Iterable<GameEntity> findByGameStatus(GameStatus status);
}
