package by.bsuir.seabattle.processor.handler;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.PlayerAction;
import by.bsuir.seabattle.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static by.bsuir.seabattle.avro.GameStatus.JOINING;
import static by.bsuir.seabattle.avro.GameStatus.RUNNING;
import static by.bsuir.seabattle.event.GameEventType.GAME_STARTED;
import static by.bsuir.seabattle.event.PlayerActionType.JOIN_GAME;

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
        String player = action.getPlayer();
        Optional<GameEvent> event = Optional.empty();
        if (!gameService.isPlayerInGame(player)) {
            Game game = gameService.find(gameId);
            if (game.getStatus() == JOINING) {
                game.getPlayers().add(player);
                game.setStatus(RUNNING);

                GameEvent gameEvent = new GameEvent();
                gameEvent.setType(GAME_STARTED);
                gameEvent.setGame(game);
                event = Optional.of(gameEvent);
            }
        }
        // TODO: 01/10/2022 GAME ALREADY RUNNING EVENT
        return event;
    }
}
