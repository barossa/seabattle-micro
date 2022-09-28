package by.bsuir.seabattle.controller;

import by.bsuir.seabattle.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static by.bsuir.seabattle.config.Topics.GAMES_EVENTS;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {
    private static final String PLAYER_KEY = "player";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody Event event) {
        Map<String, String> payload = (Map<String, String>) event.getPayload();
        String player = payload.get(PLAYER_KEY);
        ResponseEntity.BodyBuilder response;
        if (player != null) {
            kafkaTemplate.send(GAMES_EVENTS, player, event);
            response = ResponseEntity.ok();
        } else {
            response = ResponseEntity.badRequest();
        }
        return response.build();
    }

    @PostMapping("/get")
    void joinGame(@RequestBody Event event) {
        Map<String, String> payload = (Map<String, String>) event.getPayload();
        String player = payload.get(PLAYER_KEY);
        kafkaTemplate.send("game-creation-requests", player, event);
    }

}
