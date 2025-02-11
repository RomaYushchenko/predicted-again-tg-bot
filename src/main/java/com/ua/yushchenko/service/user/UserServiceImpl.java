package com.ua.yushchenko.service.user;

import com.ua.yushchenko.model.User;
import com.ua.yushchenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }


    @Override
    public void removeUser(final long userId) {
        userRepository.findById(userId)
                      .ifPresentOrElse(user -> userRepository.deleteById(user.getId()),
                                       () -> log.warn("User not found with id: " + userId));
    }

    @Override
    public List<User> findAllByNotificationsEnabled(boolean enabled) {
        return enabled ? userRepository.findAllByNotificationsEnabledTrue() : List.of();
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

    @Override
    public void saveNotificationState(long chatId, boolean enabled) {
        User user = userRepository.findByChatId(chatId)
            .orElseGet(() -> createUser(chatId));
        user.setNotificationsEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public boolean getNotificationState(long chatId) {
        return userRepository.findByChatId(chatId)
            .map(User::isNotificationsEnabled)
            .orElse(false);
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
    public Set<Long> getAllChatsWithNotifications() {
        return userRepository.findAllByNotificationsEnabledTrue().stream()
            .map(User::getChatId)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isNotificationsEnabled(long chatId) {
        return userRepository.findByChatId(chatId)
            .map(User::isNotificationsEnabled)
            .orElse(false);
    }

    @Override
    public void toggleNotifications(long chatId) {
        User user = userRepository.findByChatId(chatId)
            .orElseGet(() -> createUser(chatId));
        user.setNotificationsEnabled(!user.isNotificationsEnabled());
        userRepository.save(user);
    }

    @Override
    public Set<Long> findChatsWithNotifications(LocalDateTime time) {
        return userRepository.findAllByNotificationsEnabledTrueAndNotificationTime(time).stream()
            .map(User::getChatId)
            .collect(Collectors.toSet());
    }

    @Override
    public void saveLastNotificationTime(long chatId, LocalDateTime time) {
        User user = userRepository.findByChatId(chatId)
            .orElseGet(() -> createUser(chatId));
        user.setLastNotificationTime(time);
        userRepository.save(user);
    }

    @Override
    public Optional<LocalDateTime> getLastNotificationTime(long chatId) {
        return userRepository.findByChatId(chatId)
            .map(User::getLastNotificationTime);
    }

    private User createUser(long chatId) {
        User user = new User();
        user.setChatId(chatId);
        user.setNotificationsEnabled(true);
        user.setTimeZone("Europe/Kiev");
        return user;
    }
} 