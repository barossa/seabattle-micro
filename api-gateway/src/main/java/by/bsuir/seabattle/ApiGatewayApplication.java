package by.bsuir.seabattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    /*@Bean
    CommandLineRunner runner() {
        return args -> {
            System.out.printf("Schema:::\n\n\n%s\nSchema:::\n\n\n", ReflectData.get().getSchema(GameEvent.class).toString());
        };
    }*/

}
