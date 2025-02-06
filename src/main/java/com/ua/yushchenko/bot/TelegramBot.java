package com.ua.yushchenko.bot;

import com.ua.yushchenko.command.Command;
import com.ua.yushchenko.command.CommandFactory;
import com.ua.yushchenko.config.BotConfig.BotSettings;
import com.ua.yushchenko.service.notification.NotificationService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.telegram.TelegramBotService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main Telegram bot class that handles all incoming updates and message processing.
 * Extends TelegramLongPollingBot to receive updates from Telegram.
 * Manages command execution, message handling, and notification scheduling.
 *
 * @author AI
 * @version 0.1-beta
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    @Getter
    @Value("${bot.name}")
    private String botUsername;

    @Getter
    @Value("${bot.token}")
    private String botToken;

    private final BotSettings botSettings;
    private final ExecutorService executorService;
    private final PredictionService predictionService;
    private final NotificationService notificationService;
    private final TelegramBotService telegramBotService;
    private final CommandFactory commandFactory;

    /**
     * Creates a new TelegramBot instance with required dependencies.
     *
     * @param botSettings bot configuration containing token and username
     * @param predictionService service for generating predictions
     * @param notificationService service for managing notifications
     * @param telegramBotService service for Telegram API interactions
     * @param commandFactory factory for creating command instances
     */
    public TelegramBot(BotSettings botSettings,
                      PredictionService predictionService,
                      NotificationService notificationService,
                      TelegramBotService telegramBotService,
                      CommandFactory commandFactory) {
        super(botSettings.getToken());
        this.botSettings = botSettings;
        this.predictionService = predictionService;
        this.notificationService = notificationService;
        this.telegramBotService = telegramBotService;
        this.commandFactory = commandFactory;
        this.executorService = Executors.newFixedThreadPool(10);
        log.info("TelegramBot initialized with name: {}", botSettings.getName());
    }

    /**
     * Initializes the bot after construction.
     * Starts the notification scheduler.
     */
    @PostConstruct
    public void init() {
        log.info("Initializing bot commands...");
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Почати роботу з ботом"));
        listOfCommands.add(new BotCommand("/quick", "Отримати швидке передбачення"));
        listOfCommands.add(new BotCommand("/daily", "Отримати щоденне передбачення"));
        listOfCommands.add(new BotCommand("/settings", "Налаштування сповіщень"));
        
        try {
            execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
            log.info("Bot commands initialized successfully");
            notificationService.startNotificationScheduler();
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: {}", e.getMessage());
        }
    }

    /**
     * Handles incoming updates from Telegram.
     * Processes messages and callback queries.
     *
     * @param update the update from Telegram
     */
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                if (update.getMessage().hasText()) {
                    handleTextMessage(update);
                } else {
                    long chatId = update.getMessage().getChatId();
                    telegramBotService.sendMessage(chatId, "Вибачте, я розумію тільки текстові повідомлення.");
                }
            } else if (update.hasCallbackQuery()) {
                handleCallbackQuery(update);
            }
        } catch (Exception e) {
            log.error("Error processing update: {}", e.getMessage(), e);
            try {
                long chatId = update.hasMessage() ? update.getMessage().getChatId() :
                             update.hasCallbackQuery() ? update.getCallbackQuery().getMessage().getChatId() : 0;
                if (chatId != 0) {
                    telegramBotService.sendMessage(chatId, "Вибачте, сталася помилка. Спробуйте ще раз.");
                }
            } catch (TelegramApiException ex) {
                log.error("Error sending error message: {}", ex.getMessage(), ex);
            }
        }
    }

    /**
     * Handles incoming messages.
     * Creates and executes appropriate commands based on message content.
     *
     * @param message the message to handle
     */
    private void handleTextMessage(Update update) {
        if (update.getMessage() == null || update.getMessage().getText() == null) {
            log.error("Received null message or text in handleTextMessage");
            return;
        }

        try {
            Command command = commandFactory.createCommand(update.getMessage());
            command.execute(update);
        } catch (Exception e) {
            log.error("Error handling text message: {}", e.getMessage(), e);
            try {
                telegramBotService.sendMessage(update.getMessage().getChatId(), 
                    "Вибачте, сталася помилка при обробці повідомлення. Спробуйте ще раз.");
            } catch (TelegramApiException ex) {
                log.error("Error sending error message: {}", ex.getMessage(), ex);
            }
        }
    }

    /**
     * Handles callback queries from inline keyboard buttons.
     * Creates and executes appropriate commands based on callback data.
     *
     * @param callbackQuery the callback query to handle
     */
    private void handleCallbackQuery(Update update) {
        if (update.getCallbackQuery() == null || update.getCallbackQuery().getMessage() == null) {
            log.error("Received null callback query or message in handleCallbackQuery");
            return;
        }

        try {
            Command command = commandFactory.createCommand(update.getCallbackQuery());
            command.execute(update);
        } catch (Exception e) {
            log.error("Error handling callback query: {}", e.getMessage(), e);
            try {
                telegramBotService.sendMessage(update.getCallbackQuery().getMessage().getChatId(),
                    "Вибачте, сталася помилка при обробці запиту. Спробуйте ще раз.");
            } catch (TelegramApiException ex) {
                log.error("Error sending error message: {}", ex.getMessage(), ex);
            }
        }
    }

    /**
     * Executes a Telegram API method.
     * Overrides the parent method to add error handling.
     *
     * @param method the method to execute
     * @return result of the method execution
     * @throws TelegramApiException if there is an error executing the method
     */
    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        try {
            return super.execute(method);
        } catch (TelegramApiException e) {
            if (e.getMessage().contains("message is not modified")) {
                log.debug("Message is not modified");
                throw e;
            }
            log.error("Error executing method: {}", e.getMessage());
            throw e;
        }
    }
} 