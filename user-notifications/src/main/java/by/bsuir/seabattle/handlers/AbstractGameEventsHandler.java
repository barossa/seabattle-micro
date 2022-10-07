package by.bsuir.seabattle.handlers;

import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.GameEventType;
import by.bsuir.seabattle.event.PlayerEvent;

public abstract class AbstractGameEventsHandler {
    private final GameEventType eventType;

    protected AbstractGameEventsHandler(GameEventType eventType) {
        this.eventType = eventType;
    }

    public abstract PlayerEvent handle(GameEvent gameEvent);

    public GameEventType getEventType() {
        return eventType;
    }
}
