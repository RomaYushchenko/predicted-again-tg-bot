package com.ua.yushchenko.service.prediction;

import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implementation of PredictionStrategy for generating quick predictions.
 * Uses a predefined list of quick predictions and ensures consecutive predictions
 * for the same user are different.
 *
 * @author AI
 * @version 0.1-beta
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuickPredictionStrategy implements PredictionStrategy {
    /** List of available predictions */
    private final List<Prediction> predictions;
    
    /** Service for managing user data */
    private final UserService userService;
    
    /** Random number generator for selecting predictions */
    private final Random random = new Random();

    /**
     * {@inheritDoc}
     * Generates a quick prediction that differs from the user's last prediction.
     * If there is only one prediction available, it may return the same prediction.
     */
    @Override
    public String generatePrediction(long chatId) {
        log.debug("Generating quick prediction for chat {}", chatId);
        String lastPrediction = userService.getLastPrediction(chatId);
        String newPrediction;
        
        List<Prediction> quickPredictions = predictions.stream()
            //.filter(p -> "Загальні".equals(p.getCategory()))
            .collect(Collectors.toList());
        
        do {
            newPrediction = quickPredictions.get(random.nextInt(quickPredictions.size())).getText();
        } while (newPrediction.equals(lastPrediction) && quickPredictions.size() > 1);
        
        userService.saveLastPrediction(chatId, newPrediction);
        log.debug("Generated quick prediction for chat {}: {}", chatId, newPrediction);
        return newPrediction;
    }
} 