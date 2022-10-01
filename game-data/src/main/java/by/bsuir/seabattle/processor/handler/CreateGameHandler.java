package by.bsuir.seabattle.processor.handler;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.PlayerAction;
import by.bsuir.seabattle.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

import static by.bsuir.seabattle.avro.GameStatus.ENDED;
import static by.bsuir.seabattle.avro.GameStatus.JOINING;
import static by.bsuir.seabattle.event.GameEventType.GAME_CREATED;
import static by.bsuir.seabattle.event.PlayerActionType.CREATE_GAME;
import static by.bsuir.seabattle.processor.handler.PayloadUtil.GAME_KEY;

@Slf4j
@Component
public class CreateGameHandler extends AbstractPlayerActionHandler {

    private final GameService gameService;

    public CreateGameHandler(GameService gameService) {
        super(CREATE_GAME);
        this.gameService = gameService;
    }

    @Override
    public Optional<GameEvent> handle(String gameId, PlayerAction action) {
        Map<String, String> payload = action.getPayload();
        String player = PayloadUtil.getPlayer(payload);
        Game.Builder builder = Game.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSteps(Collections.emptyList())
                .setPlayers(Collections.singletonList(player));

        if (!gameService.isPlayerInGame(player)) {
            builder.setStatus(JOINING);
            log.info("Created new game. Player: {} ", player);
        } else {
            builder.setStatus(ENDED);
            log.info("Player {} has running other game", player);
        }

        Map<String, Object> eventPayload = new HashMap<>();
        eventPayload.put(GAME_KEY, builder.build());

        return Optional.of(new GameEvent(GAME_CREATED, eventPayload));
    }
}
