package com.ua.yushchenko.command;

import static com.ua.yushchenko.command.CommandConstants.CALLBACK_ANOTHER_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_CHANGE_TIME;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_REACTION_BAD;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_REACTION_FUNNY;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_REACTION_SUPER;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_SETTINGS;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_TOGGLE_NOTIFICATIONS;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_DAILY;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_DAILY_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_MAGIC_BALL_BUTTON;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_QUICK;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_QUICK_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_SETTINGS;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_SETTINGS_BUTTON;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_START;

import java.util.Map;

import com.ua.yushchenko.builder.ui.magicball.MagicBallButtonBuilder;
import com.ua.yushchenko.builder.ui.main.MainMenuButtonBuilder;
import com.ua.yushchenko.builder.ui.prediction.DailyPredictionButtonBuilder;
import com.ua.yushchenko.builder.ui.prediction.QuickPredictionButtonBuilder;
import com.ua.yushchenko.builder.ui.reaction.ReactionButtonBuilder;
import com.ua.yushchenko.builder.ui.settings.SettingButtonBuilder;
import com.ua.yushchenko.events.MagicBallKafkaProducer;
import com.ua.yushchenko.model.ReactionType;
import com.ua.yushchenko.service.client.ChatGptServiceClient;
import com.ua.yushchenko.service.mainmenubutton.MainMenuButtonService;
import com.ua.yushchenko.service.notification.NotificationSchedulerService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.reaction.ReactionService;
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
    private final ReactionService reactionService;
    private final ReactionButtonBuilder reactionButtonBuilder;
    private final QuickPredictionButtonBuilder quickPredictionButtonBuilder;
    private final DailyPredictionButtonBuilder dailyPredictionButtonBuilder;
    private final SettingButtonBuilder settingButtonBuilder;
    private final MainMenuButtonBuilder mainMenuButtonBuilder;
    private final MainMenuButtonService mainMenuButtonService;
    private final MagicBallButtonBuilder magicBallButtonBuilder;
    private final MagicBallKafkaProducer magicBallKafkaProducer;

    private static final Logger log = LoggerFactory.getLogger(CommandFactory.class);

    private static final Map<String, ReactionType> CALLBACK_TO_REACTION =
            Map.of(CALLBACK_REACTION_SUPER, ReactionType.SUPER,
                   CALLBACK_REACTION_FUNNY, ReactionType.FUNNY,
                   CALLBACK_REACTION_BAD, ReactionType.BAD);

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
                          final UserService userService,
                          final ReactionService reactionService,
                          final ReactionButtonBuilder reactionButtonBuilder,
                          final QuickPredictionButtonBuilder quickPredictionButtonBuilder,
                          final DailyPredictionButtonBuilder dailyPredictionButtonBuilder,
                          final SettingButtonBuilder settingButtonBuilder,
                          final MainMenuButtonBuilder mainMenuButtonBuilder,
                          final MainMenuButtonService mainMenuButtonService,
                          final MagicBallButtonBuilder magicBallButtonBuilder,
                          final MagicBallKafkaProducer magicBallKafkaProducer) {
        this.messageSender = messageSender;
        this.predictionService = predictionService;
        this.notificationSchedulerService = notificationSchedulerService;
        this.stateManager = stateManager;
        this.userService = userService;
        this.reactionService = reactionService;
        this.reactionButtonBuilder = reactionButtonBuilder;
        this.quickPredictionButtonBuilder = quickPredictionButtonBuilder;
        this.dailyPredictionButtonBuilder = dailyPredictionButtonBuilder;
        this.settingButtonBuilder = settingButtonBuilder;
        this.mainMenuButtonBuilder = mainMenuButtonBuilder;
        this.mainMenuButtonService = mainMenuButtonService;
        this.magicBallButtonBuilder = magicBallButtonBuilder;
        this.magicBallKafkaProducer = magicBallKafkaProducer;
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
                                          stateManager, notificationSchedulerService, settingButtonBuilder,
                                          mainMenuButtonBuilder, mainMenuButtonService);
        }

        if (stateManager.isAwaitingQuestion(chatId)) {
            return new MagicBallUserQuestionCommand(messageSender, chatId, magicBallKafkaProducer);
        }

        // Спочатку перевіряємо команди з емодзі
        if (text.equals(COMMAND_QUICK_PREDICTION)) {
            return new QuickPredictionCommand(messageSender, chatId, predictionService, quickPredictionButtonBuilder);
        }
        if (text.equals(COMMAND_DAILY_PREDICTION)) {
            return new DailyPredictionCommand(messageSender, chatId, predictionService, userService, dailyPredictionButtonBuilder);
        }
        if (text.equals(COMMAND_SETTINGS_BUTTON)) {
            return new SettingsCommand(messageSender, chatId, userService, stateManager, settingButtonBuilder);
        }
        if (text.equals(COMMAND_MAGIC_BALL_BUTTON)) {
            return new MagicBallCommand(messageSender, chatId, stateManager, magicBallButtonBuilder);
        }

        // Потім перевіряємо звичайні команди
        return switch (text) {
            case COMMAND_START -> new StartCommand(messageSender, chatId, notificationSchedulerService, userService, mainMenuButtonBuilder, mainMenuButtonService);
            case COMMAND_SETTINGS -> new SettingsCommand(messageSender, chatId, userService, stateManager, settingButtonBuilder);
            case COMMAND_QUICK -> new QuickPredictionCommand(messageSender, chatId, predictionService, quickPredictionButtonBuilder);
            case COMMAND_DAILY -> new DailyPredictionCommand(messageSender, chatId, predictionService, userService, dailyPredictionButtonBuilder);
            default -> new UnknownCommand(messageSender, chatId, mainMenuButtonBuilder, mainMenuButtonService);
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

        final String data = callbackQuery.getData();
        final long chatId = callbackQuery.getMessage().getChatId();
        final int messageId = callbackQuery.getMessage().getMessageId();

        final ReactionType reactionType = detectReactionType(data);

        if (reactionType != null) {
            final long callbackPredictionId = extractPredictionId(data);
            final String prefix = extractPrefix(data);

            return new ReactionCommand(messageSender, chatId, messageId, callbackPredictionId, reactionType,
                                       reactionService, reactionButtonBuilder, quickPredictionButtonBuilder, prefix);
        }

        return switch (data) {
            case CALLBACK_SETTINGS -> new SettingsCommand(messageSender, chatId, userService, stateManager, settingButtonBuilder);
            case CALLBACK_TOGGLE_NOTIFICATIONS -> new ToggleNotificationsCommand(messageSender, chatId, userService, settingButtonBuilder);
            case CALLBACK_CHANGE_TIME -> new ChangeNotificationTimeCommand(messageSender, chatId, stateManager, settingButtonBuilder);
            case CALLBACK_ANOTHER_PREDICTION -> new AnotherPredictionCommand(messageSender, chatId, messageId, predictionService, quickPredictionButtonBuilder);
            default -> new UnknownCommand(messageSender, chatId, mainMenuButtonBuilder, mainMenuButtonService);
        };
    }

    private ReactionType detectReactionType(String data) {
        return CALLBACK_TO_REACTION.entrySet()
                                   .stream()
                                   .filter(entry -> data.contains(entry.getKey()))
                                   .map(Map.Entry::getValue)
                                   .findFirst()
                                   .orElse(null);
    }

    private long extractPredictionId(String data) {
        final String[] parts = data.split("_");
        return Long.parseLong(parts[parts.length - 1]);
    }

    private String extractPrefix(String data) {
        final String[] parts = data.split("_");
        return parts[0];
    }
} 