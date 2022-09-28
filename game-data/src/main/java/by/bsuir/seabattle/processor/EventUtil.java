package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.event.Event;
import by.bsuir.seabattle.event.GameEvents;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EventUtil {
    private EventUtil() {
    }

    public static GameEvents parseEventType(Event event) {
        String typeName = event.getType().toUpperCase();
        GameEvents type;
        try {
            type = GameEvents.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown event type: {}", typeName);
            type = null;
        }
        return type;
    }
}
