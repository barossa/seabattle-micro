package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.controller.dto.GameSearchFilter;
import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.Event;
import by.bsuir.seabattle.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static by.bsuir.seabattle.event.GameEvents.FIND_GAMES;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GameViewRequestProcessor {

    private static final String STATUS_KEY = "status";
    private static final String PLAYER_KEY = "player";
    private static final String GAMES_KEY = "games";

    private final GameService gameService;

    @Bean
    public Function<KStream<String, Event>, KStream<String, Event>> processViewRequests() {
        return (events) -> {
            return events.filter((k, e) -> e.getType().equals(FIND_GAMES.name()))
                    .map((k, e) -> {
                        Map<String, String> payload = (Map<String, String>) e.getPayload();
                        String status = payload.get(STATUS_KEY);
                        String player = payload.get(PLAYER_KEY);
                        return buildNotificationEntry(status, player);
                    });
        };

    }

    private KeyValue<String, Event> buildNotificationEntry(String status, String player) {
        Map<String, Object> payload = new HashMap<>();
        List<Game> games = gameService.findAllGames(new GameSearchFilter(status));
        payload.put(GAMES_KEY, games);
        payload.put(PLAYER_KEY, player);

        Event event = Event.newBuilder()
                .setType(FIND_GAMES.name())
                .setPayload(payload)
                .build();
        return new KeyValue<>(player, event);
    }
}
