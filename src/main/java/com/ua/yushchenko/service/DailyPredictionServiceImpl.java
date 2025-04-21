package com.ua.yushchenko.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of DailyPredictionService interface.
 * Manages daily predictions and notification settings for users.
 * Uses PredictionService for generating predictions and UserService for managing user data.
 *
 * @author AI
 * @version 0.1-beta
 */
@Service
@RequiredArgsConstructor
public class DailyPredictionServiceImpl implements DailyPredictionService {

    /**
     * Service for generating predictions
     */
    private final PredictionService predictionService;

    /**
     * Service for managing user data
     */
    private final UserService userService;

    /**
     * {@inheritDoc}
     * Generates a random daily prediction and saves it as the user's last prediction.
     */
    @Override
    public String getRandomDailyPrediction(long chatId) {
        return predictionService.generateDailyPrediction(chatId);
    }

    /**
     * {@inheritDoc}
     * Toggles the notification state by getting the current state and saving its opposite.
     */
    @Override
    public void toggleNotifications(long chatId) {
        boolean currentState = isNotificationsEnabled(chatId);
        userService.saveNotificationState(chatId, !currentState);
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to get the current notification state.
     */
    @Override
    public boolean isNotificationsEnabled(long chatId) {
        return userService.isNotificationsEnabled(chatId);
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to get the notification time.
     */
    @Override
    public Optional<LocalDateTime> getNotificationTime(long chatId) {
        return userService.getNotificationTime(chatId);
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to set the notification time.
     */
    @Override
    public void setNotificationTime(long chatId, LocalDateTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Notification time cannot be null");
        }
        userService.saveNotificationTime(chatId, time);
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to get chats with notifications for a specific time.
     */
    @Override
    public Set<Long> getChatsWithNotifications(LocalDateTime time) {
        return userService.findChatsWithNotifications(time);
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to find chats with notifications for a specific time.
     */
    @Override
    public Set<Long> findChatsWithNotifications(LocalDateTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }
        return userService.findChatsWithNotifications(time);
    }

    /**
     * {@inheritDoc}
     * Checks if notifications should be sent by verifying both enabled state and time setting.
     */
    @Override
    public boolean shouldSendNotification(long chatId) {
        return isNotificationsEnabled(chatId) && getNotificationTime(chatId).isPresent();
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to enable notifications.
     */
    @Override
    public void enableNotifications(long chatId) {
        userService.saveNotificationState(chatId, true);
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to disable notifications.
     */
    @Override
    public void disableNotifications(long chatId) {
        userService.saveNotificationState(chatId, false);
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to get notification status.
     */
    @Override
    public boolean getNotificationStatus(long chatId) {
        return userService.getNotificationState(chatId);
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to get all chats with notifications.
     */
    @Override
    public Set<Long> getAllChatsWithNotifications() {
        return userService.getAllChatsWithNotifications();
    }
} 