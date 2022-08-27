package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.avro.ActivePlayerGame;
import by.bsuir.seabattle.avro.Game;
import by.bsuir.seabattle.avro.GameCreationRequest;
import by.bsuir.seabattle.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

import static by.bsuir.seabattle.avro.GameStatus.JOINING;
import static by.bsuir.seabattle.config.StreamsConfig.ACTIVE_GAMES_STORE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GameCreationRequestProcessor {
    private final GameService gameService;

    @Bean
    public Function<KStream<String, GameCreationRequest>, KStream<String, Game>> processGameCreationRequests() {
        return (requests) -> {
            KStream<String, Game> games = requests
                    .peek((k, v) -> System.out.println("Processing GCR: " + v.getLogin()))
                    .mapValues(r -> gameService.createGame(r.getLogin()))
                    .map((k, v) -> new KeyValue<>(v.getId(), v))
                    .filter((k, g) -> g.getStatus().equals(JOINING));

            games.flatMap((k, v) ->
                    v.getPlayers().stream().map((l) -> new KeyValue<>(l, new ActivePlayerGame(k))).toList()
            ).toTable(Materialized.as(ACTIVE_GAMES_STORE));

            return games;
        };
    }
}



