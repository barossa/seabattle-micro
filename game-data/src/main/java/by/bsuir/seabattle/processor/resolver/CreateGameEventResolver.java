package by.bsuir.seabattle.processor.resolver;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.Event;
import by.bsuir.seabattle.service.GameService;
import org.springframework.stereotype.Component;

import java.util.Map;

import static by.bsuir.seabattle.event.GameEvents.CREATE_GAME;

@Component
public class CreateGameEventResolver extends AbstractGameEventResolver {
    private static final String PLAYER_KEY = "player";
    private final GameService gameService;

    public CreateGameEventResolver(GameService gameService) {
        super(CREATE_GAME);
        this.gameService = gameService;
    }

    @Override
    public Game resolve(Event event) {
        Map<String, Object> payload = (Map<String, Object>) event.getPayload();
        String player = (String) payload.get(PLAYER_KEY);
        return gameService.createGame(player);
    }
}
