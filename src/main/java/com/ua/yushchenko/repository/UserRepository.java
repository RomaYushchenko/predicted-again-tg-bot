package com.ua.yushchenko.repository;

import com.ua.yushchenko.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Provides methods for querying and managing user data in the database.
 *
 * @author AI
 * @version 0.1-beta
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their Telegram chat ID.
     *
     * @param chatId the Telegram chat ID to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByChatId(Long chatId);

    /**
     * Finds all users who have notifications enabled.
     *
     * @return list of users with notifications enabled
     */
    List<User> findAllByNotificationsEnabledTrue();

    /**
     * Finds all users who have notifications enabled and set to a specific time.
     *
     * @param time the notification time to search for
     * @return list of users with notifications enabled at the specified time
     */
    List<User> findAllByNotificationsEnabledTrueAndNotificationTime(LocalDateTime time);
} 