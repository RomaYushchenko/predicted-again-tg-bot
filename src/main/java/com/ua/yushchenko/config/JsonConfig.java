package com.ua.yushchenko.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for JSON-related beans.
 * Provides configuration for JSON serialization and deserialization.
 *
 * @author AI
 * @version 0.1-beta
 */
@Configuration
public class JsonConfig {

    /**
     * Creates an ObjectMapper bean for JSON processing.
     *
     * @return configured ObjectMapper instance
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
} 