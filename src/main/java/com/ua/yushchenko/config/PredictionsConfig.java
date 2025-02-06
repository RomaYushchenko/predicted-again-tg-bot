package com.ua.yushchenko.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

/**
 * Configuration class for loading and managing predictions.
 * Loads prediction data from a JSON file and provides it as a Spring bean.
 *
 * @author AI
 * @version 0.1-beta
 */
@Slf4j
@Configuration
public class PredictionsConfig {

    /**
     * Data class for holding prediction lists.
     * Contains both quick predictions and daily predictions.
     */
    @Data
    public static class Predictions {
        /** List of quick predictions that can be sent to users */
        private List<String> quickPredictions;

        /** List of daily predictions that are sent at scheduled times */
        private List<String> dailyPredictions;
    }

    /**
     * Creates and initializes the Predictions bean.
     * Loads prediction data from the predictions.json resource file.
     *
     * @return initialized Predictions object containing all prediction lists
     * @throws RuntimeException if there is an error loading the predictions file
     */
    @Bean
    public Predictions predictions() {
        try {
            log.info("Loading predictions from predictions.json");
            ObjectMapper mapper = new ObjectMapper();
            Predictions predictions = mapper.readValue(
                new ClassPathResource("predictions.json").getInputStream(), 
                Predictions.class
            );
            log.info("Loaded {} quick predictions and {} daily predictions", 
                predictions.getQuickPredictions().size(),
                predictions.getDailyPredictions().size());
            return predictions;
        } catch (IOException e) {
            log.error("Error loading predictions: {}", e.getMessage());
            throw new RuntimeException("Could not load predictions", e);
        }
    }
} 