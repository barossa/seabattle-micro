package by.bsuir.seabattle.service;

import by.bsuir.seabattle.event.PlayerEvent;

import java.util.List;

public interface PlayerEventService {
    void push(String player, PlayerEvent event);

    List<PlayerEvent> takeEvents(String player);
}
