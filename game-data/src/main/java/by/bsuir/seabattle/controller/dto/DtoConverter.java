package by.bsuir.seabattle.controller.dto;

import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.dto.Step;

import java.util.List;

public final class DtoConverter {
    private DtoConverter() {
    }

    public static GameDto to(Game game) {
        List<StepDto> steps = game.getSteps().stream().map(DtoConverter::to).toList();
        return new GameDto(game.getId(), game.getGoing(), game.getPlayers(), game.getStatus().toString(), steps, game.getWinner());
    }

    public static StepDto to(Step step) {
        return new StepDto(step.getPlayer(), step.getX(), step.getY());
    }

}
