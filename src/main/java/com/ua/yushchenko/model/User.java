package com.ua.yushchenko.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Entity class representing a Telegram bot user.
 * Stores user preferences and notification settings.
 *
 * @author AI
 * @version 0.1-beta
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    /** Unique identifier for the user record */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Telegram chat ID associated with the user */
    private Long chatId;

    /** Time at which daily notifications should be sent */
    private LocalTime notificationTime;

    /** Whether notifications are enabled for this user */
    private boolean notificationsEnabled;

    /** The last prediction sent to this user */
    private String lastPrediction;
} 