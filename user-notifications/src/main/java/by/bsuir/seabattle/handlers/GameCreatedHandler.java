package by.bsuir.seabattle.handlers;

import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.GameEventType;
import by.bsuir.seabattle.event.PlayerEvent;
import by.bsuir.seabattle.event.PlayerEventType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GameCreatedHandler extends AbstractGameEventsHandler {
    protected GameCreatedHandler() {
        super(GameEventType.GAME_CREATED);
    }

    @Override
    public PlayerEvent handle(GameEvent gameEvent) {
        return PlayerEvent.newBuilder()
                .setType(PlayerEventType.GAME_JOINED)
                .setGames(Collections.singletonList(gameEvent.getGame()))
                .build();
    }
}
