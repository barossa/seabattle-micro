package by.bsuir.seabattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;

@SpringBootApplication
@EnableSchemaRegistryClient
public class GameDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameDataApplication.class, args);
    }

}
