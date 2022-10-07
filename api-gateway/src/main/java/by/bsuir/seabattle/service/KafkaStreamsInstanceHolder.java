package by.bsuir.seabattle.service;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

@Service
public final class KafkaStreamsInstanceHolder {
    private static final String KAFKA_STREAMS_FACTORY_BEAN_NAME = "&stream-builder-processPlayerEvents";

    private final ApplicationContext context;
    private final ReentrantLock lock;
    private final AtomicBoolean instanceExists;


    private KafkaStreams kafkaStreams;

    public KafkaStreamsInstanceHolder(ApplicationContext context) {
        this.context = context;
        lock = new ReentrantLock();
        instanceExists = new AtomicBoolean();
    }


    public KafkaStreams instance() {
        if (!instanceExists.get()) {
            lock.lock();
            try {
                if (kafkaStreams == null) {
                    initializeInstance();
                    instanceExists.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return kafkaStreams;
    }

    private void initializeInstance() {
        StreamsBuilderFactoryBean builderFactoryBean = context.getBean(KAFKA_STREAMS_FACTORY_BEAN_NAME, StreamsBuilderFactoryBean.class);
        kafkaStreams = builderFactoryBean.getKafkaStreams();
    }
}
