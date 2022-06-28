package by.bsuir.seabattle.service;

import by.bsuir.seabattle.model.Game;
import by.bsuir.seabattle.repo.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    @Value("${GAME_FIELD_HEIGHT}")
    private static int gameFieldHeight;

    @Value("${GAME_FIELD_WIDTH}")
    private static int gameFieldWidth;

    private final GameRepository gameRepository;

    @Override
    public Game createGame() {
        // TODO: 28/06/2022 CHECK FOR EXISTING PLAYER'S GAME
        // TODO: 28/06/2022 DEFINE FIRST PLAYER LOGIN FROM TOKEN
        Game game = Game.builder()
                .players(new HashSet<>())
                .steps(new HashSet<>())
                .status(Game.Status.JOINING)
                .build();
        gameRepository.save(game);
        log.info("Created new game #{}", game.getId());
        return game;
    }
}
