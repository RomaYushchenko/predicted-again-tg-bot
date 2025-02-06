package com.ua.yushchenko.service.prediction;

/**
 * Strategy interface for generating predictions.
 * Implements the Strategy pattern to allow different prediction generation algorithms.
 * Each implementation can provide its own logic for generating predictions.
 *
 * @author AI
 * @version 0.1-beta
 */
public interface PredictionStrategy {
    /**
     * Generates a prediction for a specific user.
     * The implementation should ensure that consecutive predictions for the same user are different.
     *
     * @param chatId ID of the user's chat
     * @return generated prediction text
     */
    String generatePrediction(long chatId);
} 