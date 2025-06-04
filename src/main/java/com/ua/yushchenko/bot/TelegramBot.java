package com.ua.yushchenko.bot;

import java.util.ArrayList;
import java.util.List;

import com.ua.yushchenko.command.Command;
import com.ua.yushchenko.command.CommandFactory;
import com.ua.yushchenko.config.BotConfig.BotSettings;
import com.ua.yushchenko.service.telegram.MessageSender;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
public class TelegramBot extends TelegramWebhookBot {

    @Getter
    private final String botUsername;

    @Getter
    private final String botToken;

    private final String botPath;

    private final MessageSender messageSender;
    private final CommandFactory commandFactory;

    /**
     * Creates a new TelegramBot instance with required dependencies.
     *
     * @param botSettings    bot configuration containing token and username
     * @param messageSender  service for Telegram API interactions
     * @param commandFactory factory for creating command instances
     */
    public TelegramBot(final BotSettings botSettings,
                       final MessageSender messageSender,
                       final CommandFactory commandFactory) {
        this.messageSender = messageSender;
        this.commandFactory = commandFactory;
        this.botUsername = botSettings.getName();
        this.botToken = botSettings.getToken();
        this.botPath = botSettings.getWebhookPath();
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
            messageSender.sendMessage(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
            log.info("Bot commands initialized successfully");
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: {}", e.getMessage());
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        processUpdate(update);
        return null;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    /**
     * Handles incoming updates from Telegram.
     * Processes messages and callback queries.
     *
     * @param update the update from Telegram
     */
    private void processUpdate(Update update) {
        try {
            if (update.hasMessage()) {
                if (update.getMessage().hasText()) {
                    handleTextMessage(update);
                } else {
                    long chatId = update.getMessage().getChatId();
                    messageSender.sendMessage(chatId, "Вибачте, я розумію тільки текстові повідомлення.");
                }
            } else if (update.hasCallbackQuery()) {
                handleCallbackQuery(update);
            }
        } catch (Exception e) {
            log.error("Error processing update: {}", e.getMessage(), e);
            try {
                long chatId = update.hasMessage()
                        ? update.getMessage().getChatId()
                        :
                                update.hasCallbackQuery()
                                        ? update.getCallbackQuery().getMessage().getChatId()
                                        : 0;
                if (chatId != 0) {
                    messageSender.sendMessage(chatId, "Вибачте, сталася помилка. Спробуйте ще раз.");
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
     * @param update the message to handle
     */
    private void handleTextMessage(Update update) {
        final Message message = update.getMessage();

        if (message == null || message.getText() == null) {
            log.error("Received null message or text in handleTextMessage");
            return;
        }

        try {
            log.info("User [{}] start using command: {}", message.getChatId(), message.getText());

            Command command = commandFactory.createCommand(message);
            command.execute(update);

            log.info("User [{}] finished using command: {}", message.getChatId(), message.getText());
        } catch (Exception e) {
            log.error("Error handling text message: {}", e.getMessage(), e);
            try {
                messageSender.sendMessage(message.getChatId(),
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
     * @param update the callback query to handle
     */
    private void handleCallbackQuery(Update update) {
        final CallbackQuery callbackQuery = update.getCallbackQuery();

        if (callbackQuery == null || callbackQuery.getMessage() == null) {
            log.error("Received null callback query or message in handleCallbackQuery");
            return;
        }

        try {
            log.info("User [{}] start using call back command: {}",
                     callbackQuery.getMessage().getChatId(), callbackQuery.getData());

            Command command = commandFactory.createCommand(callbackQuery);
            command.execute(update);

            log.info("User [{}] finished using call back command: {}",
                     callbackQuery.getMessage().getChatId(), callbackQuery.getData());
        } catch (Exception e) {
            log.error("Error handling callback query: {}", e.getMessage(), e);
            try {
                messageSender.sendMessage(callbackQuery.getMessage().getChatId(),
                                          "Вибачте, сталася помилка при обробці запиту. Спробуйте ще раз.");
            } catch (TelegramApiException ex) {
                log.error("Error sending error message: {}", ex.getMessage(), ex);
            }
        }
    }
}