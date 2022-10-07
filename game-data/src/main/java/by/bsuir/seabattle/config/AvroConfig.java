package by.bsuir.seabattle.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Configuration
@RequiredArgsConstructor
public class AvroConfig {

    private static final String SPECIFIC_AVRO_READER_CONFIG_VALUE = "true";
    private static final boolean IS_AVRO_SERDE_FOR_KEY = false;

    @Value("${spring.cloud.stream.kafka.streams.binder.configuration.schema.registry.url}")
    private String schemaRegistry;

    @Bean
    public GenericAvroSerde genericAvroSerde() {
        GenericAvroSerde avroSerde = new GenericAvroSerde();
        Map<String, String> properties = Map.of(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry,
                KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, SPECIFIC_AVRO_READER_CONFIG_VALUE);
        avroSerde.configure(properties, IS_AVRO_SERDE_FOR_KEY);
        return avroSerde;
    }
}
