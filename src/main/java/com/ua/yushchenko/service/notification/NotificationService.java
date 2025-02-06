package com.ua.yushchenko.service.notification;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;

/**
 * Сервіс для управління сповіщеннями користувачів.
 */
public interface NotificationService {
    /**
     * Перевіряє та надсилає сповіщення користувачам.
     * Викликається за розкладом.
     */
    void sendNotifications() throws TelegramApiException;

    /**
     * Відправляє щоденне передбачення користувачу.
     *
     * @param chatId ID чату користувача
     */
    void sendDailyPrediction(long chatId) throws TelegramApiException;

    /**
     * Запускає планувальник сповіщень.
     * Перевіряє та відправляє сповіщення всім користувачам згідно їх налаштувань.
     */
    void startNotificationScheduler();

    /**
     * Зупиняє планувальник сповіщень.
     */
    void stopNotificationScheduler();

    /**
     * Перевіряє, чи потрібно відправити сповіщення користувачу.
     *
     * @param chatId ID чату користувача
     * @return true, якщо потрібно відправити сповіщення
     */
    boolean shouldSendNotification(long chatId);

    /**
     * Встановлює час сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     * @param time час сповіщень
     */
    void setNotificationTime(long chatId, LocalTime time);

    /**
     * Вмикає сповіщення для користувача.
     *
     * @param chatId ID чату користувача
     */
    void enableNotifications(long chatId);

    /**
     * Вимикає сповіщення для користувача.
     *
     * @param chatId ID чату користувача
     */
    void disableNotifications(long chatId);

    /**
     * Перемикає стан сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     */
    void toggleNotifications(long chatId);

    /**
     * Перевіряє, чи увімкнені сповіщення для користувача.
     *
     * @param chatId ID чату користувача
     * @return true, якщо сповіщення увімкнені
     */
    boolean isNotificationsEnabled(long chatId);

    /**
     * Отримує статус сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     * @return текстовий опис статусу сповіщень
     */
    String getNotificationStatus(long chatId);
} 