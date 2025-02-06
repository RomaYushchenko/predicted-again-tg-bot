package com.ua.yushchenko.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

/**
 * Service interface for managing daily predictions and notification settings.
 * Provides functionality for generating predictions and managing user notification preferences.
 *
 * @author AI
 * @version 0.1-beta
 */
public interface DailyPredictionService {
    /**
     * Generates a random daily prediction for a specific user.
     *
     * @param chatId ID of the user's chat
     * @return generated prediction text
     */
    String getRandomDailyPrediction(long chatId);

    /**
     * Sends a daily prediction to a user.
     *
     * @param chatId ID of the user's chat
     * @throws TelegramApiException if there is an error sending the message
     */
    void sendDailyPrediction(long chatId) throws TelegramApiException;

    /**
     * Toggles the notification state for a user.
     * If notifications are enabled, they will be disabled and vice versa.
     *
     * @param chatId ID of the user's chat
     */
    void toggleNotifications(long chatId);

    /**
     * Checks if notifications are enabled for a user.
     *
     * @param chatId ID of the user's chat
     * @return true if notifications are enabled, false otherwise
     */
    boolean isNotificationsEnabled(long chatId);

    /**
     * Gets the notification time set for a user.
     *
     * @param chatId ID of the user's chat
     * @return Optional containing the notification time if set, empty otherwise
     */
    Optional<LocalTime> getNotificationTime(long chatId);

    /**
     * Sets the notification time for a user.
     *
     * @param chatId ID of the user's chat
     * @param time time at which notifications should be sent
     */
    void setNotificationTime(long chatId, LocalTime time);

    /**
     * Gets all chat IDs that have notifications scheduled for a specific time.
     *
     * @param time the time to check for notifications
     * @return set of chat IDs with notifications scheduled for the specified time
     */
    Set<Long> getChatsWithNotifications(LocalTime time);

    /**
     * Finds all chat IDs that have notifications enabled for a specific time.
     *
     * @param time the time to check for notifications
     * @return set of chat IDs with notifications enabled for the specified time
     */
    Set<Long> findChatsWithNotifications(LocalTime time);

    /**
     * Checks if a notification should be sent to a user.
     * This considers both whether notifications are enabled and if a notification time is set.
     *
     * @param chatId ID of the user's chat
     * @return true if a notification should be sent, false otherwise
     */
    boolean shouldSendNotification(long chatId);

    /**
     * Enables notifications for a user.
     *
     * @param chatId ID of the user's chat
     */
    void enableNotifications(long chatId);

    /**
     * Disables notifications for a user.
     *
     * @param chatId ID of the user's chat
     */
    void disableNotifications(long chatId);

    /**
     * Gets the current notification status for a user.
     *
     * @param chatId ID of the user's chat
     * @return true if notifications are enabled, false otherwise
     */
    boolean getNotificationStatus(long chatId);

    /**
     * Gets all chat IDs that have notifications enabled.
     *
     * @return set of chat IDs with notifications enabled
     */
    Set<Long> getAllChatsWithNotifications();
} 