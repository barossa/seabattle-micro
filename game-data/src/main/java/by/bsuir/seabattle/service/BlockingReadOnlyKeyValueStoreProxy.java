package by.bsuir.seabattle.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import java.util.function.Supplier;

@Slf4j
class BlockingReadOnlyKeyValueStoreProxy<K, V> implements ReadOnlyKeyValueStore<K, V> {
    private static final long WAIT_COUNTDOWN_MILLS = 6000L;
    private static final int MAX_RETRIES = 10;
    private final ReadOnlyKeyValueStore<K, V> store;
    private int retries;

    BlockingReadOnlyKeyValueStoreProxy(ReadOnlyKeyValueStore<K, V> store) {
        this.store = store;
    }

    @Override
    public V get(K key) {
        return callStore(() -> store.get(key));
    }

    @Override
    public KeyValueIterator<K, V> range(K from, K to) {
        return callStore(() -> store.range(from, to));
    }

    @Override
    public KeyValueIterator<K, V> reverseRange(K from, K to) {
        return callStore(() -> store.reverseRange(from, to));
    }

    @Override
    public KeyValueIterator<K, V> all() {
        return callStore(() -> store.all());
    }

    @Override
    public KeyValueIterator<K, V> reverseAll() {
        return callStore(() -> store.reverseAll());
    }

    @Override
    public <PS extends Serializer<P>, P> KeyValueIterator<K, V> prefixScan(P prefix, PS prefixKeySerializer) {
        return callStore(() -> store.prefixScan(prefix, prefixKeySerializer));
    }

    @Override
    public long approximateNumEntries() {
        return callStore(() -> store.approximateNumEntries());
    }

    private <R> R callStore(Supplier<R> supplier) {
        try {
            return supplier.get();

        } catch (InvalidStateStoreException e) {
            log.warn("Store \"{}\" not responding, waiting...", store.toString());

            try {
                Thread.sleep(WAIT_COUNTDOWN_MILLS);
            } catch (InterruptedException ex) {
                log.error("Store block interrupted");
            }

            if (retries < MAX_RETRIES) {
                retries++;
                return callStore(supplier);
            } else {
                log.error("Can't access to store by {} attempts", MAX_RETRIES);
                throw new InvalidStateStoreException(e);
            }

        }
    }
}
