package com.ua.yushchenko.service.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ua.yushchenko.model.User;

/**
 * Сервіс для роботи з користувачами.
 * Відповідає за управління користувачами та їх налаштуваннями.
 */
public interface UserService {

    User findByChatId(Long chatId);

    List<User> findAll();

    /**
     * Зберігає користувача.
     *
     * @param user користувач для збереження
     * @return збережений користувач
     */
    User save(User user);

    /**
     * Зберігає час сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     * @param time   час сповіщень
     */
    void saveNotificationTime(long chatId, LocalDateTime time);

    /**
     * Отримує час сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     * @return час сповіщень або пустий Optional, якщо не встановлено
     */
    Optional<LocalDateTime> getNotificationTime(long chatId);

    void toggleNotifications(long chatId);

    /**
     * Sets the notification time for a user.
     *
     * @param chatId ID of the user's chat
     * @param time   time at which notifications should be sent
     */
    void setNotificationTime(long chatId, LocalDateTime time);

    /**
     * Enables notifications for a user.
     *
     * @param chatId ID of the user's chat
     */
    void enableNotifications(long chatId);

    /**
     * Зберігає стан сповіщень для користувача.
     *
     * @param chatId  ID чату користувача
     * @param enabled стан сповіщень
     */
    void saveNotificationState(long chatId, boolean enabled);

    /**
     * Зберігає останнє передбачення для користувача.
     *
     * @param chatId     ID чату користувача
     * @param prediction текст передбачення
     */
    void saveLastPrediction(long chatId, String prediction);

    /**
     * Отримує останнє передбачення для користувача.
     *
     * @param chatId ID чату користувача
     * @return текст останнього передбачення або null
     */
    String getLastPrediction(long chatId);

    boolean isNotificationsEnabled(long chatId);

    /**
     * Зберігає часовий пояс для користувача.
     *
     * @param chatId   ID чату користувача
     * @param timeZone часовий пояс
     */
    void saveTimeZone(long chatId, String timeZone);

    /**
     * Отримує часовий пояс користувача.
     *
     * @param chatId ID чату користувача
     * @return часовий пояс користувача
     */
    String getTimeZone(long chatId);

    /**
     * Конвертує час з часового поясу користувача в UTC.
     *
     * @param chatId        ID чату користувача
     * @param localDateTime локальний час користувача
     * @return час в UTC
     */
    LocalDateTime convertToUTC(long chatId, LocalDateTime localDateTime);

    /**
     * Конвертує час з UTC в часовий пояс користувача.
     *
     * @param chatId      ID чату користувача
     * @param utcDateTime час в UTC
     * @return локальний час користувача
     */
    LocalDateTime convertFromUTC(long chatId, LocalDateTime utcDateTime);

    void removeUser(long userId);
} 