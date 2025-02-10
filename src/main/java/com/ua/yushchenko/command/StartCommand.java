package com.ua.yushchenko.command;

import com.ua.yushchenko.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final NotificationService notificationService;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void execute(Update update) throws TelegramApiException {
        long chatId = update.getMessage().getChatId();
        String[] args = update.getMessage().getText().split("\\s+");

        if (args.length > 1) {
            try {
                LocalDateTime notificationTime = LocalDateTime.now()
                    .withHour(Integer.parseInt(args[1].split(":")[0]))
                    .withMinute(Integer.parseInt(args[1].split(":")[1]))
                    .withSecond(0)
                    .withNano(0);

                notificationService.setNotificationTime(chatId, notificationTime);
                notificationService.enableNotifications(chatId);
            } catch (Exception e) {
                log.error("Failed to parse notification time: {}", args[1], e);
            }
        }

        notificationService.sendDailyPrediction(chatId);
    }

    @Override
    public String getCommandName() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "Почати отримувати щоденні передбачення. " +
                "Ви можете вказати час для щоденних сповіщень у форматі /start HH:mm";
    }
} 