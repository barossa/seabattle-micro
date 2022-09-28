package by.bsuir.seabattle.processor.resolver;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.Event;
import by.bsuir.seabattle.service.GameService;
import org.springframework.stereotype.Component;

import java.util.Map;

import static by.bsuir.seabattle.event.GameEvents.LEFT_GAME;

@Component
public class LeftGameEventResolver extends AbstractGameEventResolver {
    private static final String PLAYER_KEY = "player";
    private static final String GAME_KEY = "game";
    private final GameService gameService;

    public LeftGameEventResolver(GameService gameService) {
        super(LEFT_GAME);
        this.gameService = gameService;
    }

    @Override
    public Game resolve(Event event) {
        Map<String, String> payload = (Map<String, String>) event.getPayload();
        String player = payload.get(PLAYER_KEY);
        String game = payload.get(GAME_KEY);
        return gameService.leftGame(player, game);
    }
}
