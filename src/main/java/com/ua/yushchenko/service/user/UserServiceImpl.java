package com.ua.yushchenko.service.user;

import com.ua.yushchenko.model.User;
import com.ua.yushchenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of UserService.
 * Manages user data and notification settings.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void saveNotificationTime(long chatId, LocalTime time) {
        User user = userRepository.findByChatId(chatId)
                .orElseGet(() -> createUser(chatId));
        user.setNotificationTime(time);
        userRepository.save(user);
    }

    @Override
    public Optional<LocalTime> getNotificationTime(long chatId) {
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
        return getNotificationState(chatId);
    }

    @Override
    public void toggleNotifications(long chatId) {
        User user = userRepository.findByChatId(chatId)
                .orElseGet(() -> createUser(chatId));
        user.setNotificationsEnabled(!user.isNotificationsEnabled());
        userRepository.save(user);
    }

    @Override
    public Set<Long> findChatsWithNotifications(LocalTime time) {
        if (time == null) {
            return Set.of();
        }
        return userRepository.findAllByNotificationsEnabledTrueAndNotificationTime(time).stream()
                .map(User::getChatId)
                .collect(Collectors.toSet());
    }

    private User createUser(long chatId) {
        User user = new User();
        user.setChatId(chatId);
        user.setNotificationsEnabled(false);
        user.setNotificationTime(null);
        return userRepository.save(user);
    }
} 