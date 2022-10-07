package by.bsuir.seabattle.handlers;

import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.GameEventType;
import by.bsuir.seabattle.event.PlayerEvent;
import by.bsuir.seabattle.event.PlayerEventType;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class PlayerStepHandler extends AbstractGameEventsHandler {
    public PlayerStepHandler() {
        super(GameEventType.PLAYER_STEP);
    }

    @Override
    public PlayerEvent handle(GameEvent gameEvent) {
        return PlayerEvent.newBuilder()
                .setType(PlayerEventType.PLAYER_STEP)
                .setPlayer(gameEvent.getPlayer())
                .setGames(Collections.singletonList(gameEvent.getGame()))
                .build();
    }
}
