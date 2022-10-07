package by.bsuir.seabattle.controller;

import by.bsuir.seabattle.dto.DtoMapper;
import by.bsuir.seabattle.dto.PlayerEventDto;
import by.bsuir.seabattle.event.PlayerAction;
import by.bsuir.seabattle.service.PlayerEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static by.bsuir.seabattle.config.Topology.PLAYER_ACTIONS;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventsController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final PlayerEventService eventService;

    @GetMapping
    public List<PlayerEventDto> findEvents(String player) {
        return eventService.takeEvents(player).stream()
                .map(DtoMapper::to).toList();
    }

    @PostMapping
    public void sendEvent(@RequestBody PlayerAction action) {
        String game = action.getGame();
        String key = game != null ? game : "";
        kafkaTemplate.send(PLAYER_ACTIONS, key, action);
    }
}
