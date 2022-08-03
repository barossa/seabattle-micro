package by.bsuir.seabattle.config;

import by.bsuir.seabattle.processor.GenericRecordStateProcessor;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Map;

@Slf4j
@Configuration
public class StreamsConfig {

    public static final String GAMES_TOPIC = "games-topic";
    public static final String GAMES_STORE = "games-store";
    public static final String ACTIVE_GAMES_STORE = "active-games-store";

    @Bean
    public <T> JsonSerde<T> jsonSerde(@Qualifier("streamConfigGlobalProperties") Map<String, ?> streamConfigGlobalProperties) {
        JsonSerde<T> jsonSerde = new JsonSerde<>();
        jsonSerde.configure(streamConfigGlobalProperties, false);
        return jsonSerde;
    }

    @Bean
    public StreamsBuilderFactoryBeanConfigurer factoryBeanCustomizer(GenericAvroSerde avroSerde) {
        return (factoryBean) -> {
            try {
                StreamsBuilder builder = factoryBean.getObject();
                builder.addGlobalStore(
                        Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(GAMES_STORE), Serdes.String(), avroSerde),
                        GAMES_TOPIC,
                        Consumed.with(Serdes.String(), avroSerde),
                        () -> new GenericRecordStateProcessor(GAMES_STORE));

            } catch (Exception e) {
                log.error("Exception occurred customizing streams builder factory: [{}] {} ",
                        e.getClass(), e.getMessage());
            }
        };
    }
}
