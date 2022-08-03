package by.bsuir.seabattle.service;

import by.bsuir.seabattle.avro.ActivePlayerGame;
import by.bsuir.seabattle.avro.Game;
import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static by.bsuir.seabattle.avro.GameStatus.ENDED;
import static by.bsuir.seabattle.avro.GameStatus.JOINING;
import static by.bsuir.seabattle.config.StreamsConfig.ACTIVE_GAMES_STORE;
import static by.bsuir.seabattle.config.StreamsConfig.GAMES_STORE;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final String NULL = "null";

    @Value("${GAME_FIELD_HEIGHT}")
    private static int gameFieldHeight;

    @Value("${GAME_FIELD_WIDTH}")
    private static int gameFieldWidth;

    private final InteractiveQueryService queryService;

    @Override
    public Game createGame(String login) {
        Game.Builder builder = Game.newBuilder().setId(UUID.randomUUID().toString())
                .setSteps(Collections.emptyList())
                .setPlayers(Collections.singletonList(login));

        ReadOnlyKeyValueStore<String, ActivePlayerGame> activeGamesStore = queryService.getQueryableStore(ACTIVE_GAMES_STORE, QueryableStoreTypes.keyValueStore());
        ActivePlayerGame activeGame = activeGamesStore.get(login);

        if (!validateGame(activeGame)) {
            builder.setStatus(JOINING);
            log.info("Created new game. Player: {} ", login);
        } else {
            builder.setStatus(ENDED);
            log.info("Player {} has running other game #{}", login, activeGame.getId());
        }

        return builder.build();
    }

    @Override
    public List<Game> findAllGames() {
        ReadOnlyKeyValueStore<String, Game> gamesStore = queryService.getQueryableStore(GAMES_STORE, QueryableStoreTypes.keyValueStore());
        return Streams.stream(gamesStore.all()).map((e) -> e.value).collect(Collectors.toList());

    }

    private boolean validateGame(ActivePlayerGame game) {
        String gameId = game == null ? null : game.getId();
        return gameId != null && !gameId.isBlank() && !gameId.equalsIgnoreCase(NULL);
    }
}
