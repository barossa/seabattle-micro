package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.dto.GameCreationRequest;
import by.bsuir.seabattle.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

import static by.bsuir.seabattle.config.KafkaStoreConfig.ACTIVE_GAMES_STORE;
import static by.bsuir.seabattle.dto.Game.Status.JOINING;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GameCreationRequestProcessor {
    private final GameService gameService;

    @Bean
    public Function<KStream<String, GameCreationRequest>, KStream<String, Game>> processGameCreationRequests() {
        return (requests) -> {
            KStream<String, Game> games = requests.mapValues(gameService::createGame)
                    .map((k, v) -> new KeyValue<>(v.getId(), v))
                    .filter((k, g) -> g.getStatus().equals(JOINING));

            games.flatMap((k, v) ->
                    v.getPlayers().stream().map((l) -> new KeyValue<>(l, k)).toList()
            ).toTable(Materialized.as(ACTIVE_GAMES_STORE));

            return games;
        };
    }
}



