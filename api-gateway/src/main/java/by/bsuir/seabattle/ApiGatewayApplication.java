package by.bsuir.seabattle;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.support.converter.BytesJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerde;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
