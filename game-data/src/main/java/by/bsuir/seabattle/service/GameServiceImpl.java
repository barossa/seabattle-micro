package by.bsuir.seabattle.service;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.controller.dto.GameSearchFilter;
import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.List;

import static by.bsuir.seabattle.config.Topology.ACTIVE_GAMES_STORE;
import static by.bsuir.seabattle.config.Topology.GAMES_STORE;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final StoreService storeService;

    @Override
    public Game find(String id) {
        ReadOnlyKeyValueStore<String, Game> gamesStore = storeService.getReadOnlyKeyValueStore(GAMES_STORE);
        return gamesStore.get(id);
    }

    @Override
    public boolean isPlayerInGame(String player) {
        ReadOnlyKeyValueStore<Object, Object> activePlayerGamesStore = storeService.getReadOnlyKeyValueStore(ACTIVE_GAMES_STORE);
        return activePlayerGamesStore.get(player) != null;
    }

    @Override
    public List<Game> findAll(GameSearchFilter filter) {
        ReadOnlyKeyValueStore<String, Game> gamesStore = storeService.getReadOnlyKeyValueStore(GAMES_STORE);
        return Streams.stream(gamesStore.all())
                .map((p) -> p.value)
                .filter(game -> filter.status() == null || game.getStatus() == filter.status())
                .toList();
    }

}
