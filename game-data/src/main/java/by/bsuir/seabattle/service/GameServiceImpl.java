package by.bsuir.seabattle.service;

import by.bsuir.seabattle.avro.GameStatus;
import by.bsuir.seabattle.controller.dto.GameSearchFilter;
import by.bsuir.seabattle.dto.ActivePlayerGame;
import by.bsuir.seabattle.dto.Game;
import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
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

    private static final String NULL_VALUE = "null";
    private final StoreService storeService;

    @Override
    public Game createGame(String login) {
        Game.Builder builder = Game.newBuilder().setId(UUID.randomUUID().toString())
                .setSteps(Collections.emptyList())
                .setPlayers(Collections.singletonList(login));

        ReadOnlyKeyValueStore<String, ActivePlayerGame> activeGamesStore = storeService.getReadOnlyKeyValueStore(ACTIVE_GAMES_STORE);
        ActivePlayerGame activeGame = activeGamesStore.get(login);

        if (isGameNotExist(activeGame)) {
            builder.setStatus(JOINING);
            log.info("Created new game. Player: {} ", login);
        } else {
            builder.setStatus(ENDED);
            log.info("Player {} has running other game #{}", login, activeGame.getId());
        }

        return builder.build();
    }

    @Override
    public List<Game> findAllGames(GameSearchFilter filter) {
        GameStatus status = parseStatus(filter);
        ReadOnlyKeyValueStore<String, Game> gamesStore = storeService.getReadOnlyKeyValueStore(GAMES_STORE);
        return Streams.stream(gamesStore.all()).map((e) -> e.value)
                .filter(game -> status == null || game.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Game leftGame(String player, String gameId) {
        log.info("Player {} left game #{}", player, gameId);
        ReadOnlyKeyValueStore<String, Game> gamesStore = storeService.getReadOnlyKeyValueStore(GAMES_STORE);
        Game game = gamesStore.get(gameId);
        game.setStatus(ENDED);
        return game;
    }

    @Override
    public Game joinGame(String player, String gameId) {
        ReadOnlyKeyValueStore<String, ActivePlayerGame> activeGamesStore = storeService.getReadOnlyKeyValueStore(ACTIVE_GAMES_STORE);
        ReadOnlyKeyValueStore<String, Game> gamesStore = storeService.getReadOnlyKeyValueStore(GAMES_STORE);
        ActivePlayerGame activeGame = activeGamesStore.get(player);
        Game game = gamesStore.get(gameId);
        if (isGameNotExist(activeGame)) {
            game.getPlayers().add(player);
            log.info("Player {} joined game #{}", player, game);
        } else {
            log.info("Player {} has running other game #{}", player, gameId);
        }
        return game;
    }

    private boolean isGameNotExist(ActivePlayerGame game) {
        String gameId = game == null ? null : game.getId();
        return gameId == null || gameId.isBlank() || gameId.equalsIgnoreCase(NULL_VALUE);
    }

    private GameStatus parseStatus(GameSearchFilter filter) {
        try {
            return GameStatus.valueOf(filter.status().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
