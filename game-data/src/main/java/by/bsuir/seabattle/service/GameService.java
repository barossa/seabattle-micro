package by.bsuir.seabattle.service;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.dto.GameCreationRequest;

public interface GameService {
    Game createGame(GameCreationRequest request);
}
