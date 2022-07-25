package by.bsuir.seabattle.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    public static final String GAME_CREATION_REQUESTS_TOPIC = "game-creation-requests";
    public static final String ACTIVE_PLAYER_GAMES_TOPIC = "active-player-games";
    public static final String GAMES_TOPIC = "games-topic";
}
