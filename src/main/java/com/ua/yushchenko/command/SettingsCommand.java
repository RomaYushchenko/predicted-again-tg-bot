package com.ua.yushchenko.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.state.BotStateManager;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling settings menu requests.
 * Shows the current notification settings and allows users to modify them.
 *
 * @author AI
 * @version 0.1-beta
 */
public class SettingsCommand extends BaseMessageCommand {

    /**
     * Formatter for displaying notification times
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private Long predictionTimeZone;

    private final BotStateManager stateManager;

    /**
     * Creates a new settings command.
     *
     * @param bot                    the bot instance
     * @param chatId                 ID of the chat where the command was invoked
     * @param predictionService      service for generating predictions
     * @param dailyPredictionService service for handling daily predictions
     */
    public SettingsCommand(TelegramBot bot, long chatId,
                           PredictionService predictionService,
                           DailyPredictionService dailyPredictionService,
                           final BotStateManager stateManager,
                           final Long predictionTimeZone) {
        super(bot, chatId, predictionService, dailyPredictionService);
        this.stateManager = stateManager;
        this.predictionTimeZone = predictionTimeZone;
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
        final boolean notificationsEnabled = dailyPredictionService.isNotificationsEnabled(chatId);
        final StringBuilder message = new StringBuilder("⚙️ Налаштування\n\n");

        message.append(notificationsEnabled
                               ? "🔔 Сповіщення: Увімкнено"
                               : "🔕 Сповіщення: Вимкнено")
               .append("\n");

        Optional<LocalDateTime> notificationTime = dailyPredictionService.getNotificationTime(chatId);

        notificationTime.ifPresentOrElse(
                time -> message.append("🕒 Час сповіщень: ")
                               .append(time.plusHours(predictionTimeZone).format(TIME_FORMATTER)),
                () -> message.append("⚠️ Час сповіщень: Не встановлено"));


        if (update.hasMessage()) {
            sendMessage(message.toString(), createSettingsInlineKeyboard(notificationsEnabled));
        } else {
            stateManager.clearState(chatId);
            editMessage(update.getCallbackQuery().getMessage().getMessageId(),
                        message.toString(), createSettingsInlineKeyboard(notificationsEnabled));
        }
    }

    @Override
    public String getCommandName() {
        return "/settings";
    }

    @Override
    public String getDescription() {
        return "Налаштування сповіщень та часу отримання передбачень";
    }
} 