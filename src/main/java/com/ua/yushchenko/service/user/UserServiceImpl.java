package com.ua.yushchenko.service.user;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.ua.yushchenko.model.User;
import com.ua.yushchenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserService.
 * Manages user data and notification settings.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByChatId(final Long chatId) {
        final Optional<User> user = userRepository.findByChatId(chatId);

        if (user.isEmpty()) {
            log.error("User for chatId {} not found", chatId);
            return null;
        }

        return user.get();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }


    @Override
    public void removeUser(final long userId) {
        userRepository.findById(userId)
                      .ifPresentOrElse(user -> userRepository.deleteById(user.getId()),
                                       () -> log.warn("removeUser.X: User with id {} not found", userId));
    }

    @Override
    public void saveTimeZone(long chatId, String timeZone) {
        User user = userRepository.findByChatId(chatId)
                                  .orElseGet(() -> createUser(chatId));
        user.setTimeZone(timeZone);
        userRepository.save(user);
    }

    @Override
    public String getTimeZone(long chatId) {
        return userRepository.findByChatId(chatId)
                             .map(User::getTimeZone)
                             .orElse("Europe/Kiev");
    }

    @Override
    public LocalDateTime convertToUTC(long chatId, LocalDateTime localDateTime) {
        String timeZone = getTimeZone(chatId);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(timeZone));
        return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }

    @Override
    public LocalDateTime convertFromUTC(long chatId, LocalDateTime utcDateTime) {
        String timeZone = getTimeZone(chatId);
        ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
        return utcZoned.withZoneSameInstant(ZoneId.of(timeZone)).toLocalDateTime();
    }

    @Override
    public void saveNotificationTime(long chatId, LocalDateTime time) {
        User user = userRepository.findByChatId(chatId)
                                  .orElseGet(() -> createUser(chatId));
        user.setNotificationTime(time);
        userRepository.save(user);
    }

    @Override
    public Optional<LocalDateTime> getNotificationTime(long chatId) {
        return userRepository.findByChatId(chatId)
                             .map(User::getNotificationTime);
    }

    /**
     * {@inheritDoc}
     * Toggles the notification state by getting the current state and saving its opposite.
     */
    @Override
    public void toggleNotifications(long chatId) {
        boolean currentState = isNotificationsEnabled(chatId);
        saveNotificationState(chatId, !currentState);
    }

    @Override
    public void saveNotificationState(long chatId, boolean enabled) {
        User user = userRepository.findByChatId(chatId)
                                  .orElseGet(() -> createUser(chatId));
        user.setNotificationsEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public void saveLastPrediction(long chatId, String prediction) {
        User user = userRepository.findByChatId(chatId)
                                  .orElseGet(() -> createUser(chatId));
        user.setLastPrediction(prediction);
        userRepository.save(user);
    }

    @Override
    public String getLastPrediction(long chatId) {
        return userRepository.findByChatId(chatId)
                             .map(User::getLastPrediction)
                             .orElse(null);
    }

    @Override
    public boolean isNotificationsEnabled(long chatId) {
        return userRepository.findByChatId(chatId)
                             .map(User::isNotificationsEnabled)
                             .orElse(false);
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

        saveNotificationTime(chatId, time);
    }

    /**
     * {@inheritDoc}
     * Delegates to UserService to enable notifications.
     */
    @Override
    public void enableNotifications(long chatId) {
        saveNotificationState(chatId, true);
    }

    private User createUser(long chatId) {
        User user = new User();
        user.setChatId(chatId);
        user.setNotificationsEnabled(true);
        user.setTimeZone("Europe/Kiev");
        return user;
    }
} 