package by.bsuir.seabattle.service;

import by.bsuir.seabattle.dto.ActivePlayerGame;
import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.dto.GameSearchFilter;
import com.google.common.collect.Streams;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.List;

import static by.bsuir.seabattle.config.StreamsConfig.ACTIVE_GAMES_STORE;
import static by.bsuir.seabattle.config.StreamsConfig.GAMES_STORE;

@Slf4j
@Service
public class GameServiceImpl implements GameService {

    private final ReadOnlyKeyValueStore<String, Game> gamesStore;

    private final ReadOnlyKeyValueStore<String, ActivePlayerGame> activePlayerGamesStore;

    public GameServiceImpl(StoreService storeService) {
        gamesStore = storeService.getReadOnlyKeyValueStore(GAMES_STORE);
        activePlayerGamesStore = storeService.getReadOnlyKeyValueStore(ACTIVE_GAMES_STORE);
    }

    @Override
    public Game find(String id) {
        return gamesStore.get(id);
    }

    @Override
    public boolean isPlayerInGame(String player) {
        return activePlayerGamesStore.get(player) != null;
    }

    @Override
    public List<Game> findAll(GameSearchFilter filter) {
        return Streams.stream(gamesStore.all())
                .map((p) -> p.value)
                .filter(game -> filter.status() == null || game.getStatus() == filter.status())
                .toList();
    }

}
