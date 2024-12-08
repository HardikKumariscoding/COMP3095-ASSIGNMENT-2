package ca.gbc.approvalservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "ca.gbc.approvalservice")
@EnableFeignClients(basePackages = "ca.gbc.approvalservice.client")

public class ApprovalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApprovalServiceApplication.class, args);
    }

}
