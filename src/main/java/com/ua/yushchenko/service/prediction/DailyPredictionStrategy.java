package com.ua.yushchenko.service.prediction;

import com.ua.yushchenko.config.PredictionsConfig.Predictions;
import com.ua.yushchenko.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

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
    /** Configuration containing prediction lists */
    private final Predictions predictions;
    
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
        
        do {
            newPrediction = predictions.getDailyPredictions().get(
                random.nextInt(predictions.getDailyPredictions().size())
            );
        } while (newPrediction.equals(lastPrediction) && predictions.getDailyPredictions().size() > 1);
        
        userService.saveLastPrediction(chatId, newPrediction);
        log.debug("Generated daily prediction for chat {}: {}", chatId, newPrediction);
        return newPrediction;
    }
} 