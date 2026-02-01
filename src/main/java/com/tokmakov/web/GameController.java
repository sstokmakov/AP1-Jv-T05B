package com.tokmakov.web;

import com.tokmakov.domain.AuthenticationService;
import com.tokmakov.domain.model.Game;
import com.tokmakov.domain.model.User;
import com.tokmakov.domain.GameService;
import com.tokmakov.domain.mapper.GameDtoMapper;
import com.tokmakov.web.model.GameDto;
import com.tokmakov.web.model.GameCreateRequest;
import com.tokmakov.web.model.GameUpdateRequest;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {
    private final GameService service;
    private final AuthenticationService authenticationService;
    private final GameDtoMapper mapper;

    @PostMapping("/create")
    public GameDto createGame(@RequestBody GameCreateRequest request,
                              @AuthenticationPrincipal String uuid) {
        Game game = request.isVsComputer()
                ? service.createGameWithComputer(uuid)
                : service.createGameWithPlayer(uuid);
        return mapper.toDto(game);
    }

    @GetMapping("/available")
    public List<String> availableGames() {
        User user = authenticationService.getCurrentUser();
        return service.availableGames(String.valueOf(user.getUuid()));
    }

    @PostMapping("/{uuid}/join")
    public GameDto joinGame(@PathVariable("uuid") @Pattern(regexp = "[0-9a-fA-F-]{36}") String gameUuid,
                            @AuthenticationPrincipal String playerUuid) {
        Game game = service.joinGame(gameUuid, playerUuid);
        return mapper.toDto(game);
    }

    @GetMapping("/{uuid}")
    @ResponseBody
    public GameDto getGame(@PathVariable("uuid") @Pattern(regexp = "[0-9a-fA-F-]{36}") String uuid) {
        Game game = service.gameByUuid(uuid);
        return mapper.toDto(game);
    }

    @PostMapping("/{uuid}/move")
    public GameDto processTurn(@PathVariable("uuid") @Pattern(regexp = "[0-9a-fA-F-]{36}") String gameUuid,
                               @RequestBody GameUpdateRequest request,
                               @AuthenticationPrincipal String playerUuid) {
        Game game = service.processTurn(gameUuid, playerUuid, request.getX(), request.getY());
        return mapper.toDto(game);
    }
}
