package by.bsuir.seabattle.controller;

import by.bsuir.seabattle.avro.GameCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {
    private final KafkaTemplate<String, GameCreationRequest> kafkaTemplate;

    @PostMapping
    void createGame(@RequestBody GameCreationRequest request) {
        kafkaTemplate.send("game-creation-requests", request.getLogin(), request);
    }

}
