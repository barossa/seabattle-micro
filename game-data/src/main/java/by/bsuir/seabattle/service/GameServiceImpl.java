package by.bsuir.seabattle.service;

import by.bsuir.seabattle.avro.GameStatus;
import by.bsuir.seabattle.controller.dto.GameSearchFilter;
import by.bsuir.seabattle.dto.ActivePlayerGame;
import by.bsuir.seabattle.dto.Game;
import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
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
    private final StoreService storeService;

    @Override
    public Game createGame(String login) {
        Game.Builder builder = Game.newBuilder().setId(UUID.randomUUID().toString())
                .setSteps(Collections.emptyList())
                .setPlayers(Collections.singletonList(login));

        ReadOnlyKeyValueStore<String, ActivePlayerGame> activeGamesStore = storeService.getReadOnlyKeyValueStore(ACTIVE_GAMES_STORE);
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
    public List<Game> findAllGames(GameSearchFilter filter) {
        GameStatus status = parseStatus(filter);
        ReadOnlyKeyValueStore<String, Game> gamesStore = storeService.getReadOnlyKeyValueStore(GAMES_STORE);
        return Streams.stream(gamesStore.all()).map((e) -> e.value)
                .filter(game -> status == null || game.getStatus() == status)
                .collect(Collectors.toList());
    }

    private boolean validateGame(ActivePlayerGame game) {
        String gameId = game == null ? null : game.getId();
        return gameId != null && !gameId.isBlank() && !gameId.equalsIgnoreCase(NULL);
    }

    private GameStatus parseStatus(GameSearchFilter filter) {
        try {
            return GameStatus.valueOf(filter.status().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
