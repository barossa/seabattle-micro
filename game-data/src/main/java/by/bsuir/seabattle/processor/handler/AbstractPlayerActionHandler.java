package by.bsuir.seabattle.processor.handler;

import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.PlayerAction;
import by.bsuir.seabattle.event.PlayerActionType;

import java.util.Optional;

public abstract class AbstractPlayerActionHandler {
    private final PlayerActionType actionType;

    protected AbstractPlayerActionHandler(PlayerActionType actionType) {
        this.actionType = actionType;
    }

    public PlayerActionType getActionType() {
        return actionType;
    }

    public abstract Optional<GameEvent> handle(String gameId, PlayerAction action);

}
