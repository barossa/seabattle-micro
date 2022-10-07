package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.event.PlayerEvent;
import by.bsuir.seabattle.service.PlayerEventService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class PlayerEventsProcessor {

    private final PlayerEventService eventService;

    @Bean
    public Consumer<KStream<String, PlayerEvent>> processPlayerEvents() {
        return (events) -> {
            events.foreach(eventService::push);
        };
    }

}
