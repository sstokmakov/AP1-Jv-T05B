package com.tokmakov.datasource;

import com.tokmakov.domain.model.GameStatus;
import com.tokmakov.datasource.entity.GameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, UUID> {
    Iterable<GameEntity> findByGameStatus(GameStatus status);

    @Query("""
            select g
            from GameEntity g
            where g.gameStatus = com.tokmakov.domain.model.GameStatus.GAME_OVER
              and (
                    g.winnerUuid = :userUuid
                    or (g.winnerUuid is null and (g.playerXUuid = :userUuid or g.playerOUuid = :userUuid))
                  )
            """)
    List<GameEntity> findCompletedGamesByUserUuid(@Param("userUuid") UUID userUuid);

    @Query(value = """
            select stats.user_uuid as userUuid,
                   case
                       when (stats.losses + stats.draws) = 0 then stats.wins
                       else stats.wins::float / (stats.losses + stats.draws)
                       end as winRatio
            from (
                     select p.user_uuid,
                            sum(case when g.winner_uuid = p.user_uuid then 1 else 0 end) as wins,
                            sum(case when g.winner_uuid is null then 1 else 0 end) as draws,
                            sum(case when g.winner_uuid is not null and g.winner_uuid <> p.user_uuid then 1 else 0 end) as losses
                     from games g
                              join (
                         select uuid as game_id, player_x_uuid as user_uuid from games
                         union all
                         select uuid as game_id, player_o_uuid as user_uuid from games where player_o_uuid is not null
                     ) p on p.game_id = g.uuid
                     where g.game_status = 'GAME_OVER'
                     and g.vs_computer = false
                     group by p.user_uuid
                 ) stats
            order by winRatio desc
            limit :limit;
            """, nativeQuery = true)
    List<WinRatioView> findTopWinRatios(@Param("limit") int limit);
}
