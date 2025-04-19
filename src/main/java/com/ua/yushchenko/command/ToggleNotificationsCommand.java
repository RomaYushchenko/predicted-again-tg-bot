package com.ua.yushchenko.command;

import java.time.LocalDateTime;
import java.util.Optional;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ToggleNotificationsCommand extends BaseMessageCommand {

    private final Long predictionTimeZone;

    public ToggleNotificationsCommand(TelegramBot bot, long chatId,
                                      PredictionService predictionService,
                                      DailyPredictionService dailyPredictionService,
                                      final Long predictionTimeZone) {
        super(bot, chatId, predictionService, dailyPredictionService);

        this.predictionTimeZone = predictionTimeZone;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        dailyPredictionService.toggleNotifications(chatId);
        boolean wasEnabled = dailyPredictionService.isNotificationsEnabled(chatId);
        Optional<LocalDateTime> notificationTime = dailyPredictionService.getNotificationTime(chatId);

        StringBuilder message = new StringBuilder("⚙️ Налаштування\n\n");
        message.append(wasEnabled ? "🔔 Сповіщення: Увімкнено" : "🔕 Сповіщення: Вимкнено")
               .append("\n")
               .append(notificationTime.map(localDateTime -> "🕒 Час сповіщень: " + formatTime(localDateTime.plusHours(predictionTimeZone)))
                                       .orElse("⚠️ Час сповіщень: Не встановлено"));

        editMessage(update.getCallbackQuery().getMessage().getMessageId(), message.toString(),
                    createSettingsInlineKeyboard(wasEnabled));
    }

    @Override
    public String getCommandName() {
        return "toggle_notifications";
    }

    @Override
    public String getDescription() {
        return "Увімкнути/вимкнути сповіщення";
    }

    public String formatTime(LocalDateTime time) {
        if (time == null) {
            return "не встановлено";
        }
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }
} 