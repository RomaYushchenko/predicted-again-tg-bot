package com.ua.yushchenko.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "notification_time")
    private LocalDateTime notificationTime;

    /** Whether notifications are enabled for this user */
    private boolean notificationsEnabled;

    /** The last prediction sent to this user */
    @Column(name = "last_prediction")
    private String lastPrediction;

    /** Часовий пояс користувача */
    private String timeZone;

    /** Час останнього надісланого сповіщення */
    @Column(name = "last_notification_time")
    private LocalDateTime lastNotificationTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPrediction> predictions = new ArrayList<>();
} 