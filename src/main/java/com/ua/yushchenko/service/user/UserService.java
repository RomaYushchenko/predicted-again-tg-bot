package com.ua.yushchenko.service.user;

import com.ua.yushchenko.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Сервіс для роботи з користувачами.
 * Відповідає за управління користувачами та їх налаштуваннями.
 */
public interface UserService {
    /**
     * Знаходить користувача за ID.
     *
     * @param userId ID користувача
     * @return користувач
     */
    User findById(Long userId);

    /**
     * Зберігає користувача.
     *
     * @param user користувач для збереження
     * @return збережений користувач
     */
    User save(User user);

    /**
     * Знаходить всіх користувачів з увімкненими сповіщеннями.
     *
     * @param enabled стан сповіщень
     * @return список користувачів
     */
    List<User> findAllByNotificationsEnabled(boolean enabled);

    /**
     * Зберігає час сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     * @param time час сповіщень
     */
    void saveNotificationTime(long chatId, LocalDateTime time);

    /**
     * Отримує час сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     * @return час сповіщень або пустий Optional, якщо не встановлено
     */
    Optional<LocalDateTime> getNotificationTime(long chatId);

    /**
     * Зберігає стан сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     * @param enabled стан сповіщень
     */
    void saveNotificationState(long chatId, boolean enabled);

    /**
     * Отримує стан сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     * @return true, якщо сповіщення увімкнені
     */
    boolean getNotificationState(long chatId);

    /**
     * Зберігає останнє передбачення для користувача.
     *
     * @param chatId ID чату користувача
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

    /**
     * Отримує список всіх чатів, де увімкнені сповіщення.
     *
     * @return множина ID чатів
     */
    Set<Long> getAllChatsWithNotifications();

    /**
     * Перевіряє, чи увімкнені сповіщення для користувача.
     *
     * @param chatId ID чату користувача
     * @return true, якщо сповіщення увімкнені
     */
    boolean isNotificationsEnabled(long chatId);

    /**
     * Перемикає стан сповіщень для користувача.
     *
     * @param chatId ID чату користувача
     */
    void toggleNotifications(long chatId);

    /**
     * Знаходить всі чати з увімкненими сповіщеннями на вказаний час.
     *
     * @param time час сповіщень
     * @return множина ID чатів
     */
    Set<Long> findChatsWithNotifications(LocalDateTime time);

    /**
     * Зберігає часовий пояс для користувача.
     *
     * @param chatId ID чату користувача
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
     * @param chatId ID чату користувача
     * @param localDateTime локальний час користувача
     * @return час в UTC
     */
    LocalDateTime convertToUTC(long chatId, LocalDateTime localDateTime);

    /**
     * Конвертує час з UTC в часовий пояс користувача.
     *
     * @param chatId ID чату користувача
     * @param utcDateTime час в UTC
     * @return локальний час користувача
     */
    LocalDateTime convertFromUTC(long chatId, LocalDateTime utcDateTime);

    /**
     * Зберігає час останнього сповіщення для користувача.
     *
     * @param chatId ID чату користувача
     * @param time час останнього сповіщення
     */
    void saveLastNotificationTime(long chatId, LocalDateTime time);

    /**
     * Отримує час останнього сповіщення для користувача.
     *
     * @param chatId ID чату користувача
     * @return час останнього сповіщення або пустий Optional, якщо сповіщень ще не було
     */
    Optional<LocalDateTime> getLastNotificationTime(long chatId);

    void removeUser(long userId);
} 