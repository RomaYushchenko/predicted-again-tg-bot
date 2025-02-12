package com.ua.yushchenko.service.prediction;

import com.ua.yushchenko.model.Prediction;
import java.util.List;

/**
 * Service interface for loading and managing predictions.
 * Handles loading predictions from file and database operations.
 *
 * @author AI
 * @version 0.1-beta
 */
public interface PredictionLoaderService {
    /**
     * Loads predictions from the database or file if database is empty.
     * If the database is empty, predictions are loaded from the JSON file
     * and saved to the database.
     */
    void loadPredictions();

    /**
     * Checks if predictions are already loaded in the database.
     *
     * @return true if predictions exist, false otherwise
     */
    boolean arePredictionsLoaded();
} 