package by.bsuir.seabattle.service;

import by.bsuir.seabattle.controller.dto.GameSearchFilter;
import by.bsuir.seabattle.dto.Game;

import java.util.List;

public interface GameService {
    Game createGame(String login);

    List<Game> findAllGames(GameSearchFilter filter);
}
