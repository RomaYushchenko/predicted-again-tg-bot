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
     * Gets all predictions from the database.
     *
     * @return list of all predictions
     */
    List<Prediction> getAllPredictions();

    /**
     * Gets predictions by category.
     *
     * @param category the category to filter by
     * @return list of predictions in the specified category
     */
    List<Prediction> getPredictionsByCategory(String category);

    /**
     * Saves a new prediction to the database.
     *
     * @param prediction the prediction to save
     * @return the saved prediction
     */
    Prediction savePrediction(Prediction prediction);

    /**
     * Checks if predictions are already loaded in the database.
     *
     * @return true if predictions exist, false otherwise
     */
    boolean arePredictionsLoaded();
} 