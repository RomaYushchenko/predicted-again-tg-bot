package com.ua.yushchenko.service.prediction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.repository.PredictionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    @Cacheable(value = "predictions")
    public List<Prediction> getAllPredictions() {
        return predictionRepository.findAll();
    }

    @Override
    @Cacheable(value = "predictions", key = "#category")
    public List<Prediction> getPredictionsByCategory(String category) {
        return predictionRepository.findAllByCategory(category);
    }

    @Override
    @CacheEvict(value = "predictions", allEntries = true)
    public Prediction savePrediction(Prediction prediction) {
        prediction.setCreatedAt(LocalDateTime.now());
        prediction.setUpdatedAt(LocalDateTime.now());
        return predictionRepository.save(prediction);
    }

    @Override
    public boolean arePredictionsLoaded() {
        return predictionRepository.existsBy();
    }

    private Set<Prediction> loadPredictionsFromFile() throws IOException {
        var resource = new ClassPathResource("predictions.json");
        return objectMapper.readValue(
            resource.getInputStream(),
            new TypeReference<Set<Prediction>>() {}
        );
    }
} 