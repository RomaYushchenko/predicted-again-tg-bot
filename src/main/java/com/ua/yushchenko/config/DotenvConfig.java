package com.ua.yushchenko.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration class for loading environment variables from .env file.
 * Uses dotenv-java library to load variables before Spring context initialization.
 *
 * @author AI
 * @version 0.1-beta
 */
@Configuration
public class DotenvConfig {

    /**
     * Creates and configures Dotenv instance.
     * Loads environment variables from .env file in the project root.
     *
     * @return configured Dotenv instance
     */
    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }
} 