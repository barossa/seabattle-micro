package by.bsuir.seabattle.service;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.dto.GameSearchFilter;

import java.util.List;

public interface GameService {
    Game find(String id);

    boolean isPlayerInGame(String player);

    List<Game> findAll(GameSearchFilter filter);
}
