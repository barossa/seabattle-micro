package by.bsuir.seabattle.processor.resolver;

import by.bsuir.seabattle.event.Event;

public abstract class AbstractEventResolver<R> {
    private final String eventTypeName;

    protected AbstractEventResolver(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public abstract R resolve(Event event);
}
