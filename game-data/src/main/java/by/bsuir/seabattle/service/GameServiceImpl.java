package by.bsuir.seabattle.service;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.dto.GameCreationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

import static by.bsuir.seabattle.config.KafkaStoreConfig.ACTIVE_GAMES_STORE;
import static by.bsuir.seabattle.config.KafkaStoreConfig.GAMES_STORE;
import static by.bsuir.seabattle.dto.Game.Status.ENDED;
import static by.bsuir.seabattle.dto.Game.Status.JOINING;

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
    public Game createGame(GameCreationRequest request) {
        String login = request.login();
        Game.GameBuilder builder = Game.builder().id(UUID.randomUUID().toString())
                .players(Collections.singleton(login));

        ReadOnlyKeyValueStore<String, String> activeGamesStore = queryService.getQueryableStore(ACTIVE_GAMES_STORE, QueryableStoreTypes.keyValueStore());
        String gameId = activeGamesStore.get(login);

        if (!validateGameId(gameId)) {
            builder.status(JOINING);
            log.info("Created new game. Player: {} ", login);
        } else {
            builder.status(ENDED);
            log.info("Player {} has running other game #{}", login, gameId);
        }

        return builder.build();
    }

    private boolean validateGameId(String gameId) {
        return gameId != null && !gameId.isBlank() && !gameId.equalsIgnoreCase(NULL);
    }
}
