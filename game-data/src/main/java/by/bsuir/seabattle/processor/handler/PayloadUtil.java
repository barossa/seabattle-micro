package by.bsuir.seabattle.processor.handler;

import by.bsuir.seabattle.dto.Game;

import java.util.Map;

public final class PayloadUtil {
    public static final String GAME_KEY = "game";
    private static final String PLAYER_KEY = "player";

    private PayloadUtil() {
    }

    public static Game getGame(Map<String, Object> payload) {
        return (Game) payload.get(GAME_KEY);
    }

    public static String getPlayer(Map<String, ?> payload) {
        return (String) payload.get(PLAYER_KEY);
    }
}
