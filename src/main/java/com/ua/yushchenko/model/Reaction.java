package com.ua.yushchenko.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entity class representing a Telegram bot Reaction to prediction.
 * Stores Reaction to predictions preferences and notification settings.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "reactions")
public class Reaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "prediction_id", nullable = false)
    private Long predictionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType reactionType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
