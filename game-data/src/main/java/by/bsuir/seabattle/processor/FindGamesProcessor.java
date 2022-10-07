package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.avro.GameStatus;
import by.bsuir.seabattle.controller.dto.GameSearchFilter;
import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.PlayerAction;
import by.bsuir.seabattle.event.PlayerEvent;
import by.bsuir.seabattle.service.GameService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

import static by.bsuir.seabattle.event.PlayerActionType.FIND_GAMES;
import static by.bsuir.seabattle.event.PlayerEventType.GAMES_LIST;

@Configuration
@RequiredArgsConstructor
public class FindGamesProcessor {
    private static final GameStatus DEFAULT_GAME_STATUS = GameStatus.JOINING;

    private final GameService gameService;

    @Bean
    public Function<KStream<String, PlayerAction>, KStream<String, PlayerEvent>> processViewRequests() {
        return (events) -> events.filter((k, action) -> action.getType() == FIND_GAMES)
                .map((k, action) -> {
                    List<Game> games = gameService.findAll(new GameSearchFilter(DEFAULT_GAME_STATUS));
                    PlayerEvent event = PlayerEvent.newBuilder()
                            .setType(GAMES_LIST)
                            .setGames(games)
                            .build();
                    return new KeyValue<>(action.getPlayer(), event);
                });
    }

}
