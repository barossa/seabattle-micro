package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.dto.ActivePlayerGame;
import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.Event;
import by.bsuir.seabattle.event.GameEvents;
import by.bsuir.seabattle.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

import static by.bsuir.seabattle.avro.GameStatus.ENDED;
import static by.bsuir.seabattle.config.StreamsConfig.ACTIVE_GAMES_STORE;
import static by.bsuir.seabattle.config.StreamsConfig.GAMES_STORE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GameCreationRequestProcessor {
    private static final String PLAYER_KEY = "player";

    private final GameService gameService;

    @Bean
    public Function<KStream<String, Event>, KStream<String, Game>> processGameCreationRequests() {

        return (events) -> {
            KStream<String, Game> games = events
                    .filter((k, e) -> e.getType().equals(GameEvents.CREATE_GAME.name()))
                    .peek((k, e) -> System.out.println("Processing GCR: " + e))
                    .mapValues(e -> {
                        Map<String, String> payload = (Map<String, String>) e.getPayload();
                        String player = payload.get(PLAYER_KEY);
                        return gameService.createGame(player);
                    })
                    .map((k, v) -> new KeyValue<>(v.getId(), v));

            games.filter((k, game) -> game.getStatus() != ENDED)
                    .flatMap((k, v) ->
                            v.getPlayers().stream().map((l) -> new KeyValue<>(l, new ActivePlayerGame(k))).toList()
                    ).toTable(Materialized.as(ACTIVE_GAMES_STORE));

            games.toTable(Materialized.as(GAMES_STORE));

            return games;
        };
    }
}



