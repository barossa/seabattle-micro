package by.bsuir.seabattle.service;

import by.bsuir.seabattle.event.PlayerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class PlayerEventServiceImpl implements PlayerEventService {
    private final Map<String, LinkedBlockingQueue<PlayerEvent>> playerEvents = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void push(String player, PlayerEvent event) {
        log.info("Pushing {} event to {} player", event.getType(), player);
        LinkedBlockingQueue<PlayerEvent> events = playerEvents.get(player);
        try {
            lock.lock();
            if (events == null) {
                events = new LinkedBlockingQueue<>();
            }
        } finally {
            lock.unlock();
        }

        try {
            events.put(event);
            playerEvents.put(player, events);
        } catch (InterruptedException e) {
            log.warn("Can't push player event", e);
        }
    }

    @Override
    public List<PlayerEvent> takeEvents(String player) {
        LinkedBlockingQueue<PlayerEvent> events = playerEvents.get(player);
        return events != null ? new ArrayList<>(events) : Collections.emptyList();
    }
}
