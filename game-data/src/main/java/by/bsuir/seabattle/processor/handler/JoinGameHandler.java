package by.bsuir.seabattle.processor.handler;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.PlayerAction;
import by.bsuir.seabattle.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static by.bsuir.seabattle.avro.GameStatus.JOINING;
import static by.bsuir.seabattle.avro.GameStatus.RUNNING;
import static by.bsuir.seabattle.event.GameEventType.GAME_STARTED;
import static by.bsuir.seabattle.event.PlayerActionType.JOIN_GAME;
import static by.bsuir.seabattle.processor.handler.PayloadUtil.GAME_KEY;

@Slf4j
@Component
public class JoinGameHandler extends AbstractPlayerActionHandler {

    private final GameService gameService;

    public JoinGameHandler(GameService gameService) {
        super(JOIN_GAME);
        this.gameService = gameService;
    }

    @Override
    public Optional<GameEvent> handle(String gameId, PlayerAction action) {
        Map<String, String> payload = action.getPayload();
        String player = PayloadUtil.getPlayer(payload);
        Optional<GameEvent> event;
        if (!gameService.isPlayerInGame(player)) {
            Game game = gameService.find(gameId);
            if (game.getStatus() == JOINING) {
                game.getPlayers().add(player);
                game.setStatus(RUNNING);

                Map<String, Object> eventPayload = new HashMap<>();
                eventPayload.put(GAME_KEY, game);
                event = Optional.of(new GameEvent(GAME_STARTED, eventPayload));
            } else {
                // TODO: 01/10/2022 GAME ALREADY RUNNING EVENT
                event = Optional.empty();
            }

        } else {
            event = Optional.empty();
        }
        return event;
    }
}
