package by.bsuir.seabattle.processor.resolver;

import by.bsuir.seabattle.controller.dto.GameSearchFilter;
import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.Event;
import by.bsuir.seabattle.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.bsuir.seabattle.event.GameEvents.FIND_GAMES;

@Slf4j
@Component
public class FindGamesEventResolver extends AbstractEventResolver<Event> {
    private static final String PLAYER_KEY = "player";
    private static final String STATUS_KEY = "status";
    private static final String GAMES_KEY = "games";

    private final GameService gameService;

    public FindGamesEventResolver(GameService gameService) {
        super(FIND_GAMES.name());
        this.gameService = gameService;
    }

    @Override
    public Event resolve(Event event) {
        Map<String, String> payload = (Map<String, String>) event.getPayload();
        String player = payload.get(PLAYER_KEY);
        String status = payload.get(STATUS_KEY);
        List<Game> games = gameService.findAllGames(new GameSearchFilter(status));
        log.info("Found {} games by status \"{}\"", games.size(), status);

        Map<String, Object> responsePayload = new HashMap<>();
        responsePayload.put(GAMES_KEY, games);
        responsePayload.put(PLAYER_KEY, player);

        return Event.newBuilder()
                .setType(FIND_GAMES.name())
                .setPayload(responsePayload)
                .build();
    }
}
