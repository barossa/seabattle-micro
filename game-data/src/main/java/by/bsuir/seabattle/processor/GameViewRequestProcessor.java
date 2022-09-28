package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.event.Event;
import by.bsuir.seabattle.processor.resolver.FindGamesEventResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

import static by.bsuir.seabattle.event.GameEvents.FIND_GAMES;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GameViewRequestProcessor {
    private static final String PLAYER_KEY = "player";

    private final FindGamesEventResolver findGamesEventResolver;
    @Bean
    public Function<KStream<String, Event>, KStream<String, Event>> processViewRequests() {
        return (events) -> {
            return events.filter((k, e) -> EventUtil.parseEventType(e) == FIND_GAMES)
                    .map((k, e) -> {
                        Map<String, String> payload = (Map<String, String>) e.getPayload();
                        String player = payload.get(PLAYER_KEY);
                        return new KeyValue<>(player, findGamesEventResolver.resolve(e));
                    });
        };

    }
}
