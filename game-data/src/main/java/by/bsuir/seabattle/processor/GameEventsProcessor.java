package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.dto.ActivePlayerGame;
import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.Event;
import by.bsuir.seabattle.event.GameEvents;
import by.bsuir.seabattle.processor.resolver.AbstractGameEventResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

import static by.bsuir.seabattle.avro.GameStatus.ENDED;
import static by.bsuir.seabattle.config.StreamsConfig.ACTIVE_GAMES_STORE;
import static by.bsuir.seabattle.config.StreamsConfig.GAMES_STORE;
import static by.bsuir.seabattle.event.GameEvents.FIND_GAMES;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GameEventsProcessor {
    private final List<AbstractGameEventResolver> resolvers;
    @Bean
    public Function<KStream<String, Event>, KStream<String, Game>> processGameEvents() {

        return (events) -> {
            Predicate<String, Event> isCompatible = (k, event) -> {
                GameEvents type = EventUtil.parseEventType(event);
                return type != null && type != FIND_GAMES;
            };

            KStream<String, Game> games = events
                    .filter(isCompatible)
                    .peek((k, e) -> System.out.println("Processing game event: " + e))
                    .mapValues(e -> {
                        GameEvents type = EventUtil.parseEventType(e);
                        AbstractGameEventResolver eventResolver = resolvers.stream().filter((r) -> r.getEventType() == type)
                                .findFirst().orElseThrow(() -> new RuntimeException("Can't find resolver of event type " + type));
                        return eventResolver.resolve(e);
                    })
                    .map((k, v) -> new KeyValue<>(v.getId(), v));

            games.flatMap((k, v) -> {
                ActivePlayerGame game = v.getStatus() != ENDED ? new ActivePlayerGame(k) : null;
                return v.getPlayers().stream().map((player) -> new KeyValue<>(player, game)).toList();
            }).toTable(Materialized.as(ACTIVE_GAMES_STORE));

            games.toTable(Materialized.as(GAMES_STORE));

            return games;
        };
    }


}



