package com.ua.yushchenko.service.notification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.ua.yushchenko.model.User;
import com.ua.yushchenko.service.MessageSender;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

                String prediction = predictionService.generateDailyPrediction(user.getChatId());

                try {
                    messageSender.sendMessage(user.getChatId(), prediction);

                    user.setLastNotificationTime(now);
                    userService.save(user);

                    log.info("sendDailyPrediction.X: Successfully sent notification to user {}", user.getChatId());
                } catch (TelegramApiException e) {
                    if (e.getMessage().contains("bot was blocked by the user")) {
                        userService.removeUser(user.getId());
                        log.info("sendDailyPrediction.X: The user {} who blocked the bot was deleted",
                                 user.getChatId());
                        continue;
                    }

                    log.error("Failed to send daily prediction to user {}: {}", user.getChatId(), e.getMessage());
                } catch (Exception e) {
                    log.error("Unexpected exception: Failed to send daily prediction to user {}: {}",
                              user.getChatId(), e.getMessage());
                }

            }
        }
    }

    @Override
    public void startNotificationScheduler() {
        log.info("Notification scheduler started");
    }

    @Override
    public void setNotificationTime(long chatId, LocalDateTime time) {
        userService.saveNotificationTime(chatId, time);
    }

    @Override
    public void toggleNotifications(long chatId) {
        userService.toggleNotifications(chatId);
    }

    @Override
    public boolean isNotificationsEnabled(long chatId) {
        return userService.isNotificationsEnabled(chatId);
    }

    private boolean shouldSendNotification(User user, LocalDateTime now) {
        return isTimeToSendNotification(user.getNotificationTime(), now);
    }

    private boolean isTimeToSendNotification(LocalDateTime notificationTime, LocalDateTime now) {
        if (notificationTime == null) {
            return false;
        }

        LocalTime scheduledTime = notificationTime.toLocalTime().minusHours(1);
        LocalTime currentTime = now.toLocalTime().withSecond(0).withNano(0);

        return currentTime.equals(scheduledTime);
    }
} 