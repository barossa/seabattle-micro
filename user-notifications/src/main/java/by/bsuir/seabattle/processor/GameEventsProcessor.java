package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.GameEventType;
import by.bsuir.seabattle.event.PlayerEvent;
import by.bsuir.seabattle.handlers.AbstractGameEventsHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameEventsProcessor {

    private final List<AbstractGameEventsHandler> handlers;

    @Bean
    public Function<KStream<String, GameEvent>, KStream<String, PlayerEvent>> processGameEvents() {
        return (events) -> {
            return events.flatMap((k, event) -> {
                Optional<AbstractGameEventsHandler> handler = defineHandler(event.getType());
                Iterable<KeyValue<String, PlayerEvent>> playerEvents;
                if (handler.isPresent()) {
                    PlayerEvent playerEvent = handler.get().handle(event);
                    playerEvents = event.getGame().getPlayers().stream()
                            .map((player) -> new KeyValue<>(player, playerEvent))
                            .toList();
                } else {
                    log.warn("No player event for {} game event", event.getType());
                    playerEvents = Collections.emptyList();
                }
                return playerEvents;
            });
        };
    }

    private Optional<AbstractGameEventsHandler> defineHandler(GameEventType eventType) {
        return handlers.stream().filter(h -> h.getEventType() == eventType).findFirst();
    }

}
