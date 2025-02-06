package com.ua.yushchenko;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main application class for the Predictions Telegram Bot.
 * Configures Spring Boot application and loads environment variables.
 *
 * @author AI
 * @version 0.1-beta
 */
@SpringBootApplication
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