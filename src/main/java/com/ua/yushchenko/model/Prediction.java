package com.ua.yushchenko.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity class representing a prediction.
 * Stores prediction text and category.
 *
 * @author AI
 * @version 0.1-beta
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "predictions")
public class Prediction {
    /** Unique identifier for the prediction */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The text of the prediction */
    private String text;

    /** The category of the prediction (e.g., "Любов", "Кар'єра", "Фінанси") */
    private String category;

    /** When the prediction was created */
    private LocalDateTime createdAt;

    /** When the prediction was last updated */
    private LocalDateTime updatedAt;
} 