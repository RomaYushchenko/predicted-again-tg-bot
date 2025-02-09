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
 * Implementation of PredictionStrategy for generating daily predictions.
 * Uses a predefined list of daily predictions and ensures consecutive predictions
 * for the same user are different.
 *
 * @author AI
 * @version 0.1-beta
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DailyPredictionStrategy implements PredictionStrategy {
    /** List of available predictions */
    private final List<Prediction> predictions;
    
    /** Service for managing user data */
    private final UserService userService;
    
    /** Random number generator for selecting predictions */
    private final Random random = new Random();

    /**
     * {@inheritDoc}
     * Generates a daily prediction that differs from the user's last prediction.
     * If there is only one prediction available, it may return the same prediction.
     */
    @Override
    public String generatePrediction(long chatId) {
        log.debug("Generating daily prediction for chat {}", chatId);
        String lastPrediction = userService.getLastPrediction(chatId);
        String newPrediction;
        
        List<Prediction> dailyPredictions = predictions.stream()
            //.filter(p -> !"Загальні".equals(p.getCategory()))
            .collect(Collectors.toList());
        
        do {
            newPrediction = dailyPredictions.get(random.nextInt(dailyPredictions.size())).getText();
        } while (newPrediction.equals(lastPrediction) && dailyPredictions.size() > 1);
        
        userService.saveLastPrediction(chatId, newPrediction);
        log.debug("Generated daily prediction for chat {}: {}", chatId, newPrediction);
        return newPrediction;
    }
} 