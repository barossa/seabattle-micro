package by.bsuir.seabattle.controller;

import by.bsuir.seabattle.avro.Game;
import by.bsuir.seabattle.controller.dto.DtoConverter;
import by.bsuir.seabattle.controller.dto.GameDto;
import by.bsuir.seabattle.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public List<GameDto> findGames() {
        List<Game> games = gameService.findAllGames();
        return games.stream().map(DtoConverter::to).toList();
    }

}
