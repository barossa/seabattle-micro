package by.bsuir.seabattle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.support.serializer.JsonSerde;

@SpringBootApplication
@Slf4j
public class GameDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameDataApplication.class, args);
    }

}
