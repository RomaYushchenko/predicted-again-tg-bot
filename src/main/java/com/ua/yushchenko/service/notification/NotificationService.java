package com.ua.yushchenko.service.notification;

import com.ua.yushchenko.model.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

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
     * Запускає планувальник сповіщень.
     * Перевіряє та відправляє сповіщення всім користувачам згідно їх налаштувань.
     */
    void startNotificationScheduler();


    /**
     * Встановлює час сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     * @param time час сповіщень
     */
    void setNotificationTime(long chatId, LocalDateTime time);

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
} 