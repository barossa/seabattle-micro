package by.bsuir.seabattle.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class KafkaStoreConfig {
    public static final String GAMES_STORE = "games-store";

    public static final String ACTIVE_GAMES_STORE = "active-games-store";

}
