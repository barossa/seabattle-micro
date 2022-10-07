package by.bsuir.seabattle.dto;

import java.util.List;

public record PlayerEventDto(String type, List<GameDto> games, GameDto.StepDto step, String player) {
}
