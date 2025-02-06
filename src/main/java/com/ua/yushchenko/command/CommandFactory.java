package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.notification.NotificationService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.telegram.TelegramBotService;
import com.ua.yushchenko.state.BotStateManager;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ua.yushchenko.command.CommandConstants.*;

/**
 * Factory class responsible for creating appropriate command instances based on user interactions.
 * Handles both message commands and callback query commands.
 *
 * @author AI
 * @version 0.1-beta
 */
public class CommandFactory {
    private final TelegramBot bot;
    private final PredictionService predictionService;
    private final DailyPredictionService dailyPredictionService;
    private final NotificationService notificationService;
    private final TelegramBotService telegramBotService;
    private final BotStateManager stateManager;
    private static final Logger log = LoggerFactory.getLogger(CommandFactory.class);

    /**
     * Creates a new CommandFactory instance.
     *
     * @param bot the main bot instance
     * @param predictionService service for generating predictions
     * @param dailyPredictionService service for handling daily predictions
     * @param notificationService service for managing notifications
     * @param telegramBotService service for Telegram API interactions
     * @param stateManager manager for bot states
     */
    public CommandFactory(TelegramBot bot,
                        PredictionService predictionService,
                        DailyPredictionService dailyPredictionService,
                        NotificationService notificationService,
                        TelegramBotService telegramBotService,
                        BotStateManager stateManager) {
        this.bot = bot;
        this.predictionService = predictionService;
        this.dailyPredictionService = dailyPredictionService;
        this.notificationService = notificationService;
        this.telegramBotService = telegramBotService;
        this.stateManager = stateManager;
    }

    /**
     * Creates a command instance based on a message.
     * If the user is in time input state, returns a TimeMessageCommand.
     * Otherwise, creates a command based on the message text.
     *
     * @param message the message from the user
     * @return appropriate Command instance
     */
    public Command createCommand(Message message) {
        if (message == null || message.getText() == null) {
            log.error("Received null message or text in createCommand");
            throw new IllegalArgumentException("Message or text cannot be null");
        }

        String text = message.getText().trim();
        long chatId = message.getChatId();

        // Check if user is awaiting time input
        if (stateManager.isAwaitingTime(chatId)) {
            return new TimeMessageCommand(bot, message, predictionService, dailyPredictionService, stateManager);
        }

        // Спочатку перевіряємо команди з емодзі
        if (text.equals(COMMAND_QUICK_PREDICTION)) {
            return new QuickPredictionCommand(bot, chatId, predictionService, dailyPredictionService);
        }
        if (text.equals(COMMAND_DAILY_PREDICTION)) {
            return new DailyPredictionCommand(bot, chatId, predictionService, dailyPredictionService);
        }
        if (text.equals(COMMAND_SETTINGS_BUTTON)) {
            return new SettingsCommand(bot, chatId, predictionService, dailyPredictionService);
        }

        // Потім перевіряємо звичайні команди
        return switch (text) {
            case COMMAND_START -> new StartCommand(bot, chatId, predictionService, dailyPredictionService);
            case COMMAND_SETTINGS -> new SettingsCommand(bot, chatId, predictionService, dailyPredictionService);
            case COMMAND_QUICK -> new QuickPredictionCommand(bot, chatId, predictionService, dailyPredictionService);
            case COMMAND_DAILY -> new DailyPredictionCommand(bot, chatId, predictionService, dailyPredictionService);
            default -> new UnknownCommand(bot, chatId, predictionService, dailyPredictionService);
        };
    }

    /**
     * Creates a command instance based on a callback query.
     * Handles various callback actions like settings, notifications, and menu navigation.
     *
     * @param callbackQuery the callback query from an inline keyboard button
     * @return appropriate Command instance
     */
    public Command createCommand(CallbackQuery callbackQuery) {
        if (callbackQuery == null || callbackQuery.getMessage() == null) {
            log.error("Received null callback query or message in createCommand");
            throw new IllegalArgumentException("Callback query and message cannot be null");
        }

        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        return switch (data) {
            case CALLBACK_SETTINGS -> new SettingsCommand(bot, chatId, predictionService, dailyPredictionService);
            case CALLBACK_TOGGLE_NOTIFICATIONS -> new ToggleNotificationsCommand(bot, chatId, predictionService, dailyPredictionService);
            case CALLBACK_CHANGE_TIME -> new ChangeNotificationTimeCommand(telegramBotService, notificationService, stateManager);
            case CALLBACK_MENU -> new BackToMainMenuCommand(bot, chatId, messageId, predictionService, dailyPredictionService);
            case CALLBACK_ANOTHER_PREDICTION -> new AnotherPredictionCommand(bot, chatId, messageId, predictionService, dailyPredictionService);
            case CALLBACK_ANOTHER_DAILY -> new AnotherDailyPredictionCommand(bot, chatId, messageId, predictionService, dailyPredictionService);
            default -> new UnknownCommand(bot, chatId, predictionService, dailyPredictionService);
        };
    }
} 