package com.ua.yushchenko.service.notification;

import com.ua.yushchenko.model.User;
import com.ua.yushchenko.service.MessageSender;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Реалізація сервісу сповіщень.
 * Відповідає за відправку щоденних передбачень та управління розкладом сповіщень.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserService userService;
    private final PredictionService predictionService;
    private final MessageSender messageSender;

    @Override
    @Scheduled(cron = "0 * * * * *") // Кожну хвилину
    public void sendNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<User> users = userService.findAllByNotificationsEnabled(true);

        for (User user : users) {
            if (user.isNotificationsEnabled()
                    && user.getNotificationTime() != null && shouldSendNotification(user, now)) {
                sendDailyPrediction(user.getId());
                updateLastNotificationTime(user.getId(), now);
            }
        }
    }

    @Override
    public void sendDailyPrediction(Long userId) {
        User user = userService.findById(userId);
        String prediction = predictionService.generateDailyPrediction(user.getChatId());
        try {
            messageSender.sendMessage(user.getChatId(), prediction);
            log.info("sendDailyPrediction.X: Successfully sent notification to user {}", user.getChatId());
        } catch (TelegramApiException e) {

            if (e.getMessage().contains("bot was blocked by the user")){
                userService.removeUser(userId);
                log.info("sendDailyPrediction.X: The user {} who blocked the bot was deleted", user.getChatId());
                return;
            }

            log.error("Failed to send daily prediction to user {}: {}", user.getChatId(), e.getMessage());
        }
    }

    @Override
    public void updateNotificationTime(Long userId, LocalDateTime notificationTime) {
        User user = userService.findById(userId);
        user.setNotificationTime(notificationTime);
        userService.save(user);
    }

    @Override
    public void updateLastNotificationTime(Long userId, LocalDateTime lastNotificationTime) {
        User user = userService.findById(userId);
        user.setLastNotificationTime(lastNotificationTime);
        userService.save(user);
    }

    @Override
    public void startNotificationScheduler() {
        log.info("Notification scheduler started");
    }

    @Override
    public void stopNotificationScheduler() {
        log.info("Notification scheduler stopped");
    }

    @Override
    public boolean shouldSendNotification(long chatId) {
        User user = userService.findById(chatId);
        return user.isNotificationsEnabled() && user.getNotificationTime() != null;
    }

    @Override
    public void setNotificationTime(long chatId, LocalDateTime time) {
        userService.saveNotificationTime(chatId, time);
    }

    @Override
    public void enableNotifications(long chatId) {
        userService.saveNotificationState(chatId, true);
    }

    @Override
    public void disableNotifications(long chatId) {
        userService.saveNotificationState(chatId, false);
    }

    @Override
    public void toggleNotifications(long chatId) {
        userService.toggleNotifications(chatId);
    }

    @Override
    public boolean isNotificationsEnabled(long chatId) {
        return userService.isNotificationsEnabled(chatId);
    }

    @Override
    public String getNotificationStatus(long chatId) {
        boolean enabled = userService.isNotificationsEnabled(chatId);
        if (!enabled) {
            return "🔕 Сповіщення вимкнені";
        }

        return userService.getNotificationTime(chatId)
            .map(time -> "🔔 Сповіщення увімкнені\n⏰ Час сповіщень: " + formatTime(time.plusHours(2)))
            .orElse("🔔 Сповіщення увімкнені\n⚠️ Час сповіщень не встановлено");
    }

    @Override
    public String formatTime(LocalDateTime time) {
        if (time == null) {
            return "не встановлено";
        }
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }

    private boolean shouldSendNotification(User user, LocalDateTime now) {
        return isTimeToSendNotification(user.getNotificationTime(), now);
    }

    private boolean isTimeToSendNotification(LocalDateTime notificationTime, LocalDateTime now) {
        if (notificationTime == null) {
            return false;
        }

        LocalTime scheduledTime = notificationTime.toLocalTime();
        LocalTime currentTime = now.toLocalTime();

        return currentTime.isAfter(scheduledTime.minusMinutes(1)) && 
               currentTime.isBefore(scheduledTime.plusMinutes(1));
    }

    private boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        return date1.toLocalDate().equals(date2.toLocalDate());
    }
} 