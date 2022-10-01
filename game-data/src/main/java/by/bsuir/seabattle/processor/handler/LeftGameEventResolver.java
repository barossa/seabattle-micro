package by.bsuir.seabattle.processor.handler;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.PlayerAction;
import by.bsuir.seabattle.service.GameService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static by.bsuir.seabattle.avro.GameStatus.ENDED;
import static by.bsuir.seabattle.event.GameEventType.PLAYER_LEFT;
import static by.bsuir.seabattle.event.PlayerActionType.LEFT_GAME;
import static by.bsuir.seabattle.processor.handler.PayloadUtil.GAME_KEY;

@Component
public class LeftGameEventResolver extends AbstractPlayerActionHandler {
    private final GameService gameService;

    public LeftGameEventResolver(GameService gameService) {
        super(LEFT_GAME);
        this.gameService = gameService;
    }

    @Override
    public Optional<GameEvent> handle(String gameId, PlayerAction action) {
        Game game = gameService.find(gameId);
        String player = PayloadUtil.getPlayer(action.getPayload());
        Optional<GameEvent> event;
        if (game.getPlayers().contains(player)) {
            game.setStatus(ENDED);
            Map<String, Object> payload = new HashMap<>();
            payload.put(GAME_KEY, game);
            event = Optional.of(new GameEvent(PLAYER_LEFT, payload));

        } else {
            event = Optional.empty();
        }
        return event;
    }
}
