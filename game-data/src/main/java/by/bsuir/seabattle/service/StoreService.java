package by.bsuir.seabattle.service;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

public interface StoreService {
    <K,V> ReadOnlyKeyValueStore<K,V> getReadOnlyKeyValueStore(String name);
}
