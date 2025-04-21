package com.ua.yushchenko.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ToggleNotificationsCommand extends BaseMessageCommand {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ToggleNotificationsCommand(TelegramBot bot, long chatId,
                                      PredictionService predictionService,
                                      DailyPredictionService dailyPredictionService) {
        super(bot, chatId, predictionService, dailyPredictionService);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        dailyPredictionService.toggleNotifications(chatId);
        boolean wasEnabled = dailyPredictionService.isNotificationsEnabled(chatId);
        Optional<LocalDateTime> notificationTime = dailyPredictionService.getNotificationTime(chatId);

        final StringBuilder message = new StringBuilder("⚙️ Налаштування\n\n");
        message.append(wasEnabled
                               ? "🔔 Сповіщення: Увімкнено"
                               : "🔕 Сповіщення: Вимкнено")
               .append("\n")
               .append(notificationTime.map(time -> "🕒 Час сповіщень: " + time.format(TIME_FORMATTER))
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
} 