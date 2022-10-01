package by.bsuir.seabattle.processor;

import by.bsuir.seabattle.dto.ActivePlayerGame;
import by.bsuir.seabattle.dto.Game;
import by.bsuir.seabattle.event.GameEvent;
import by.bsuir.seabattle.event.PlayerAction;
import by.bsuir.seabattle.event.PlayerActionType;
import by.bsuir.seabattle.processor.handler.AbstractPlayerActionHandler;
import by.bsuir.seabattle.processor.handler.PayloadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

import static by.bsuir.seabattle.avro.GameStatus.ENDED;
import static by.bsuir.seabattle.config.StreamsConfig.ACTIVE_GAMES_STORE;
import static by.bsuir.seabattle.config.StreamsConfig.GAMES_STORE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PlayerActionsProcessor {
    private final List<AbstractPlayerActionHandler> handlers;

    @Bean
    public Function<KStream<String, PlayerAction>, KStream<String, GameEvent>> processPlayerActions() {

        return (actions) -> {
            KStream<String, GameEvent> processed = actions
                    .peek((k, a) -> System.out.println("Processing player action: " + a))
                    .map((k, a) -> {
                        AbstractPlayerActionHandler handler = defineHandler(a.getType());
                        return new KeyValue<>(k, handler.handle(k, a));
                    })
                    .filter((k, v) -> v.isPresent())
                    .map((k, v) -> {
                        GameEvent event = v.get();
                        Game game = PayloadUtil.getGame(event.getPayload());
                        return new KeyValue<>(game.getId(), event);
                    });

            KStream<String, Game> games = processed.map((k, event) -> {
                Game game = PayloadUtil.getGame(event.getPayload());
                return new KeyValue<>(game.getId(), game);
            });

            games.flatMap((k, v) -> {
                ActivePlayerGame game = v.getStatus() != ENDED ? new ActivePlayerGame(k) : null;
                return v.getPlayers().stream().map((player) -> new KeyValue<>(player, game)).toList();
            }).toTable(Materialized.as(ACTIVE_GAMES_STORE));

            games.toTable(Materialized.as(GAMES_STORE));

            return processed;
        };
    }

    private AbstractPlayerActionHandler defineHandler(PlayerActionType actionType) {
        return handlers.stream().filter((r) -> r.getActionType() == actionType)
                .findFirst().orElseThrow(() -> new RuntimeException("Can't find handler of action type " + actionType));
    }

}



