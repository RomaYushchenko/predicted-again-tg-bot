package com.ua.yushchenko.service.prediction;

import java.util.Optional;

import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.model.User;

/**
 * Service interface for generating predictions.
 * Provides methods for generating both quick and daily predictions.
 * Supports dynamic addition and removal of prediction strategies.
 *
 * @author AI
 * @version 0.1-beta
 */
public interface PredictionService {

    Optional<Prediction> getPredictionByText(final String predictionText);

    /**
     * Generates a quick prediction for a user.
     * Quick predictions are shorter and more immediate in nature.
     *
     * @param chatId ID of the user's chat
     * @return generated quick prediction text
     */
    Prediction generateQuickPrediction(long chatId);

    /**
     * Generates an unique prediction for a user.
     *
     * @param chatId ID of the user's chat
     * @return generated unique prediction
     */
    Prediction generateUniquePrediction(final long chatId);

    /**
     * Save user prediction
     *
     * @param user       instance of user
     * @param prediction instance of prediction
     */
    void saveUserPrediction(final User user, final String prediction);
} 