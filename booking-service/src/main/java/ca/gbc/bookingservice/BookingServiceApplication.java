package ca.gbc.bookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.CircuitBreaker;


@SpringBootApplication(scanBasePackages = "ca.gbc.bookingservice")
@EnableFeignClients("ca.gbc.bookingservice.client")
@CircuitBreaker
public class BookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }

}
