package com.ua.yushchenko.repository;

import java.util.List;
import java.util.Optional;

import com.ua.yushchenko.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Prediction entity operations.
 * Provides methods for querying and managing predictions in the database.
 *
 * @author AI
 * @version 0.1-beta
 */
@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    /**
     * Finds all predictions by category.
     *
     * @param category the category to search for
     * @return list of predictions in the specified category
     */
    List<Prediction> findAllByCategory(String category);

    Optional<Prediction> findPredictionByText(final String text);

    /**
     * Checks if any predictions exist in the database.
     *
     * @return true if there are predictions, false otherwise
     */
    boolean existsBy();
} 