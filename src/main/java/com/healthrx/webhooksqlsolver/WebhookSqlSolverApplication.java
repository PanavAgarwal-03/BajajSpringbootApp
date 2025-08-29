package com.healthrx.webhooksqlsolver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.healthrx.webhooksqlsolver.service.WebhookService;

@SpringBootApplication
public class WebhookSqlSolverApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookSqlSolverApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(WebhookService webhookService) {
        return args -> {
            try {
                webhookService.executeProcess();
            } catch (Exception e) {
                System.err.println("Error executing webhook process: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        };
    }
}
