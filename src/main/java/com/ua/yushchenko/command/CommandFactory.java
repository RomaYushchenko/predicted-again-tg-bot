package com.ua.yushchenko.command;

import static com.ua.yushchenko.command.CommandConstants.CALLBACK_ANOTHER_DAILY;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_ANOTHER_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_CHANGE_TIME;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_SETTINGS;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_TOGGLE_NOTIFICATIONS;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_DAILY;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_DAILY_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_QUICK;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_QUICK_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_SETTINGS;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_SETTINGS_BUTTON;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_START;

import com.ua.yushchenko.service.notification.NotificationSchedulerService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.service.user.UserService;
import com.ua.yushchenko.state.BotStateManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Factory class responsible for creating appropriate command instances based on user interactions.
 * Handles both message commands and callback query commands.
 *
 * @author AI
 * @version 0.1-beta
 */
@Slf4j
@Component
public class CommandFactory {

    private final MessageSender messageSender;
    private final PredictionService predictionService;
    private final NotificationSchedulerService notificationSchedulerService;
    private final BotStateManager stateManager;
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(CommandFactory.class);

    /**
     * Creates a new CommandFactory instance.
     *
     * @param messageSender                the main bot instance
     * @param predictionService            service for generating predictions
     * @param notificationSchedulerService service for managing notifications
     * @param stateManager                 manager for bot states
     */
    public CommandFactory(final MessageSender messageSender,
                          final PredictionService predictionService,
                          final NotificationSchedulerService notificationSchedulerService,
                          final BotStateManager stateManager,
                          final UserService userService) {
        this.messageSender = messageSender;
        this.predictionService = predictionService;
        this.notificationSchedulerService = notificationSchedulerService;
        this.stateManager = stateManager;
        this.userService = userService;
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
            return new TimeMessageCommand(messageSender, message, userService,
                                          stateManager, notificationSchedulerService);
        }

        // Спочатку перевіряємо команди з емодзі
        if (text.equals(COMMAND_QUICK_PREDICTION)) {
            return new QuickPredictionCommand(messageSender, chatId, predictionService);
        }
        if (text.equals(COMMAND_DAILY_PREDICTION)) {
            return new DailyPredictionCommand(messageSender, chatId, predictionService);
        }
        if (text.equals(COMMAND_SETTINGS_BUTTON)) {
            return new SettingsCommand(messageSender, chatId, userService, stateManager);
        }

        // Потім перевіряємо звичайні команди
        return switch (text) {
            case COMMAND_START -> new StartCommand(messageSender, chatId, notificationSchedulerService, userService);
            case COMMAND_SETTINGS -> new SettingsCommand(messageSender, chatId, userService, stateManager);
            case COMMAND_QUICK -> new QuickPredictionCommand(messageSender, chatId, predictionService);
            case COMMAND_DAILY -> new DailyPredictionCommand(messageSender, chatId, predictionService);
            default -> new UnknownCommand(messageSender, chatId);
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
            case CALLBACK_SETTINGS -> new SettingsCommand(messageSender, chatId, userService, stateManager);
            case CALLBACK_TOGGLE_NOTIFICATIONS -> new ToggleNotificationsCommand(messageSender, chatId, userService);
            case CALLBACK_CHANGE_TIME -> new ChangeNotificationTimeCommand(messageSender, chatId, stateManager);
            case CALLBACK_ANOTHER_PREDICTION ->
                    new AnotherPredictionCommand(messageSender, chatId, messageId, predictionService);
            case CALLBACK_ANOTHER_DAILY ->
                    new AnotherDailyPredictionCommand(messageSender, chatId, messageId, predictionService);
            default -> new UnknownCommand(messageSender, chatId);
        };
    }
} 