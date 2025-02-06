package com.ua.yushchenko.service.notification;

import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.MessageSender;
import com.ua.yushchenko.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Реалізація сервісу сповіщень.
 * Відповідає за відправку щоденних передбачень та управління розкладом сповіщень.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final DailyPredictionService dailyPredictionService;
    private final UserService userService;
    private final MessageSender messageSender;
    private final TaskScheduler taskScheduler;
    
    // Використовуємо один глобальний замок для синхронізації
    private final Lock notificationLock = new ReentrantLock();
    
    // Зберігаємо час останнього сповіщення для кожного користувача
    private final Map<Long, LocalTime> lastNotificationTimes = new ConcurrentHashMap<>();
    
    // Зберігаємо останню оброблену хвилину
    private volatile LocalTime lastProcessedMinute;

    @Override
    @Scheduled(cron = "0 * * * * *") // Кожну хвилину
    public void sendNotifications() throws TelegramApiException {
        // Спроба отримати замок без блокування
        if (!notificationLock.tryLock()) {
            log.debug("Notification task is already running");
            return;
        }

        try {
            LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
            
            // Перевіряємо, чи не обробляли ми вже цю хвилину
            if (lastProcessedMinute != null && lastProcessedMinute.equals(currentTime)) {
                log.debug("This minute ({}) has already been processed", currentTime);
                return;
            }

            log.debug("Processing notifications for time: {}", currentTime);
            Set<Long> chatsToNotify = userService.findChatsWithNotifications(currentTime);
            
            if (chatsToNotify.isEmpty()) {
                log.debug("No chats to notify at {}", currentTime);
                return;
            }

            log.debug("Found {} chats to notify at {}", chatsToNotify.size(), currentTime);

            for (Long chatId : chatsToNotify) {
                try {
                    // Перевіряємо час останнього сповіщення
                    LocalTime lastNotificationTime = lastNotificationTimes.get(chatId);
                    if (lastNotificationTime != null && 
                        Math.abs(ChronoUnit.MINUTES.between(currentTime, lastNotificationTime)) < 2) {
                        log.debug("Skipping notification for chat {} as last notification was at {}", 
                                chatId, lastNotificationTime);
                        continue;
                    }

                    // Перевіряємо, чи дійсно потрібно відправляти сповіщення
                    if (!dailyPredictionService.shouldSendNotification(chatId)) {
                        log.debug("Notifications are disabled for chat {}", chatId);
                        continue;
                    }

                    sendDailyPrediction(chatId);
                    lastNotificationTimes.put(chatId, currentTime);
                    log.info("Successfully sent notification to chat {}", chatId);
                    
                } catch (Exception e) {
                    log.error("Failed to send notification to chat {}: {}", chatId, e.getMessage());
                }
            }

            // Очищаємо старі записи (старші за 2 години)
            cleanupOldNotifications(currentTime);
            
            // Зберігаємо останню оброблену хвилину
            lastProcessedMinute = currentTime;
            
        } finally {
            notificationLock.unlock();
        }
    }

    private void cleanupOldNotifications(LocalTime currentTime) {
        lastNotificationTimes.entrySet().removeIf(entry -> 
            Math.abs(ChronoUnit.MINUTES.between(currentTime, entry.getValue())) > 120);
    }

    @Override
    public void sendDailyPrediction(long chatId) throws TelegramApiException {
        String prediction = dailyPredictionService.getRandomDailyPrediction(chatId);
        Optional<LocalTime> notificationTime = userService.getNotificationTime(chatId);
        
        if (notificationTime.isPresent()) {
            String message = String.format("🌟 Ваше щоденне передбачення на %s:\n\n%s", 
                    formatTime(notificationTime.get()), prediction);
            messageSender.sendMessage(chatId, message);
        }
    }

    @Override
    public void startNotificationScheduler() {
        log.info("Notification scheduler is running via @Scheduled annotation");
    }

    @Override
    public void stopNotificationScheduler() {
        log.info("Notification scheduler is managed by Spring");
    }

    @Override
    public boolean shouldSendNotification(long chatId) {
        if (!isNotificationsEnabled(chatId)) {
            return false;
        }

        Optional<LocalTime> notificationTime = userService.getNotificationTime(chatId);
        if (notificationTime.isEmpty()) {
            return false;
        }

        LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        return notificationTime.get().equals(currentTime);
    }

    @Override
    public void setNotificationTime(long chatId, LocalTime time) {
        userService.saveNotificationTime(chatId, time);
        log.info("Set notification time to {} for chat {}", time, chatId);
    }

    @Override
    public void enableNotifications(long chatId) {
        userService.saveNotificationState(chatId, true);
        log.info("Enabled notifications for chat {}", chatId);
    }

    @Override
    public void disableNotifications(long chatId) {
        userService.saveNotificationState(chatId, false);
        log.info("Disabled notifications for chat {}", chatId);
    }

    @Override
    public void toggleNotifications(long chatId) {
        boolean currentState = userService.getNotificationState(chatId);
        userService.saveNotificationState(chatId, !currentState);
        log.info("{} notifications for chat {}", currentState ? "Disabled" : "Enabled", chatId);
    }

    @Override
    public boolean isNotificationsEnabled(long chatId) {
        return userService.getNotificationState(chatId);
    }

    @Override
    public String getNotificationStatus(long chatId) {
        boolean enabled = userService.isNotificationsEnabled(chatId);
        if (!enabled) {
            return "🔕 Сповіщення вимкнені";
        }

        Optional<LocalTime> time = userService.getNotificationTime(chatId);
        return time.map(t -> String.format("🔔 Сповіщення увімкнені\n⏰ Час сповіщень: %s", formatTime(t)))
                .orElse("🔔 Сповіщення увімкнені\n⏰ Час сповіщень не встановлено");
    }

    private String formatTime(LocalTime time) {
        int hour = time.getHour();
        String period;

        if (hour >= 4 && hour < 12) {
            period = "ранок";
        } else if (hour >= 12 && hour < 17) {
            period = "день";
        } else if (hour >= 17 && hour < 22) {
            period = "вечір";
        } else {
            period = "ніч";
        }

        return String.format("%02d:%02d (%s)", hour, time.getMinute(), period);
    }
} 