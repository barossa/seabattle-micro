package by.bsuir.seabattle.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.schema.registry.client.ConfluentSchemaRegistryClient;
import org.springframework.cloud.schema.registry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Configuration
public class SchemaRegistryClientConfig {

    private static final String SPECIFIC_AVRO_READER_CONFIG_VALUE = "true";
    private static final boolean IS_AVRO_SERDE_FOR_KEY = false;

    @Value("${spring.cloud.stream.kafka.streams.binder.configuration.schema.registry.url}")
    private String endpoint;

    @Primary
    @Bean
    public SchemaRegistryClient schemaRegistryClient() {
        ConfluentSchemaRegistryClient client = new ConfluentSchemaRegistryClient();
        client.setEndpoint(endpoint);
        return client;
    }

    @Bean
    public GenericAvroSerde genericAvroSerde() {
        GenericAvroSerde avroSerde = new GenericAvroSerde();
        Map<String, String> properties = Map.of(SCHEMA_REGISTRY_URL_CONFIG, endpoint,
                KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, SPECIFIC_AVRO_READER_CONFIG_VALUE);
        avroSerde.configure(properties, IS_AVRO_SERDE_FOR_KEY);
        return avroSerde;
    }
}
