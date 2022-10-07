package by.bsuir.seabattle.dto;

import by.bsuir.seabattle.event.PlayerEvent;

import java.util.List;

public final class DtoMapper {
    private DtoMapper() {
    }

    public static PlayerEventDto to(PlayerEvent event) {
        List<GameDto> games = event.getGames().stream().map(DtoMapper::to).toList();

        return new PlayerEventDto(event.getType().name(), games,
                to(event.getStep()), event.getPlayer());
    }

    public static GameDto to(Game game) {
        List<GameDto.StepDto> steps = game.getSteps().stream().map(DtoMapper::to).toList();
        return new GameDto(game.getId(), game.getStatus().name(), game.getPlayers(),
                game.getGoing(), game.getWinner(), steps);
    }

    public static GameDto.StepDto to(Step step) {
        return step != null ? new GameDto.StepDto(step.getX(), step.getY(), step.getPlayer()) : new GameDto.StepDto(0, 0, "");
    }
}
