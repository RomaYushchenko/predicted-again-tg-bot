package com.ua.yushchenko;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for the Predictions Telegram Bot.
 * Configures Spring Boot application and loads environment variables.
 *
 * @author AI
 * @version 0.1-beta
 */
@EnableRetry
@EnableAsync
@SpringBootApplication(scanBasePackages = "com.ua.yushchenko")
public class PredictionsApplication {

    static {
        // Load environment variables before Spring context initialization
        Dotenv.configure()
              .ignoreIfMissing()
              .load();
    }

    public static void main(String[] args) {
        SpringApplication.run(PredictionsApplication.class, args);
    }
} 