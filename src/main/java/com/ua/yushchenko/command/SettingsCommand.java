package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Command for handling settings menu requests.
 * Shows the current notification settings and allows users to modify them.
 *
 * @author AI
 * @version 0.1-beta
 */
public class SettingsCommand extends BaseMessageCommand {
    /** Formatter for displaying notification times */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Creates a new settings command.
     *
     * @param bot the bot instance
     * @param chatId ID of the chat where the command was invoked
     * @param predictionService service for generating predictions
     * @param dailyPredictionService service for handling daily predictions
     */
    public SettingsCommand(TelegramBot bot, long chatId,
                         PredictionService predictionService,
                         DailyPredictionService dailyPredictionService) {
        super(bot, chatId, predictionService, dailyPredictionService);
    }

    /**
     * Executes the settings command.
     * Shows current notification settings and available options.
     *
     * @param update the update containing the command request
     * @throws TelegramApiException if there is an error sending the message
     */
    @Override
    public void execute(Update update) throws TelegramApiException {
        boolean notificationsEnabled = dailyPredictionService.isNotificationsEnabled(chatId);
        StringBuilder message = new StringBuilder("⚙️ Налаштування\n\n");
        message.append("🔔 Сповіщення: ").append(notificationsEnabled ? "Увімкнено" : "Вимкнено").append("\n");
        
        if (notificationsEnabled) {
            Optional<LocalTime> notificationTime = dailyPredictionService.getNotificationTime(chatId);
            if (notificationTime.isPresent()) {
                message.append("🕒 Час сповіщень: ").append(notificationTime.get().format(TIME_FORMATTER));
            }
        }
        
        sendMessage(message.toString(), createSettingsInlineKeyboard(notificationsEnabled));
    }
} 