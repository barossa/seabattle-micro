package by.bsuir.seabattle.controller.dto;

import java.util.List;

public record GameDto(String id, String going, List<String> players,
                      String status, List<StepDto> steps, String winner) {

}
