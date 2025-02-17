package com.ua.yushchenko.service.prediction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.repository.PredictionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PredictionLoaderService.
 * Handles loading predictions from JSON file and managing them in the database.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionLoaderServiceImpl implements PredictionLoaderService {

    private final PredictionRepository predictionRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        loadPredictions();
    }

    @Override
    @Transactional
    public void loadPredictions() {
        if (!arePredictionsLoaded()) {
            log.info("Loading predictions from file...");
            try {
                Set<Prediction> predictions = loadPredictionsFromFile();
                LocalDateTime now = LocalDateTime.now();
                predictions.forEach(prediction -> {
                    prediction.setCreatedAt(now);
                    prediction.setUpdatedAt(now);
                });
                predictionRepository.saveAll(predictions);
                log.info("Successfully loaded {} predictions", predictions.size());
            } catch (IOException e) {
                log.error("Error loading predictions from file: {}", e.getMessage());
                throw new RuntimeException("Failed to load predictions", e);
            }
        } else {
            log.info("Predictions are already loaded in the database");
        }
    }

    @Override
    public boolean arePredictionsLoaded() {
        return predictionRepository.existsBy();
    }

    private Set<Prediction> loadPredictionsFromFile() throws IOException {
        var resource = new ClassPathResource("predictions.json");
        return objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<Set<Prediction>>() {
                }
                                     );
    }
} 