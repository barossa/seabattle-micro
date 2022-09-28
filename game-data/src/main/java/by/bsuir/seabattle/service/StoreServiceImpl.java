package by.bsuir.seabattle.service;

import lombok.AllArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final InteractiveQueryService queryService;

    @Override
    public <K, V> ReadOnlyKeyValueStore<K, V> getReadOnlyKeyValueStore(String name) {
        ReadOnlyKeyValueStore<K, V> store = queryService.getQueryableStore(name, QueryableStoreTypes.keyValueStore());
        return new BlockingReadOnlyKeyValueStoreProxy<>(store);
    }
}
