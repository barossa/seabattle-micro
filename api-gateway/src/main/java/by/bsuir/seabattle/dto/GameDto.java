package by.bsuir.seabattle.dto;

import java.util.List;

public record GameDto(String id, String status, List<String> players, String going, String winner,
                      List<StepDto> steps) {

    public record StepDto(int x, int y, String player) {
    }

}
