package by.bsuir.seabattle.controller;

import by.bsuir.seabattle.event.PlayerAction;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static by.bsuir.seabattle.config.Topics.PLAYER_ACTIONS;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {
    private static final String GAME_KEY = "game";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    public void sendEvent(@RequestBody PlayerAction action) {
        Map<String, String> payload = (Map<String, String>) action.getPayload();
        String game = payload.get(GAME_KEY);
        kafkaTemplate.send(PLAYER_ACTIONS, game, action);
    }
}
