package by.bsuir.seabattle.processor.resolver;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.Event;
import by.bsuir.seabattle.event.GameEvents;

public abstract class AbstractGameEventResolver extends AbstractEventResolver<Game> {
    private final GameEvents eventType;

    protected AbstractGameEventResolver(GameEvents eventType) {
        super(eventType.name());
        this.eventType = eventType;
    }

    public GameEvents getEventType() {
        return eventType;
    }

    public abstract Game resolve(Event event);

}
