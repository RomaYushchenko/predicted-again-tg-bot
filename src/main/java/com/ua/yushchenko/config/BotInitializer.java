
package com.ua.yushchenko.config;

import com.ua.yushchenko.bot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Initializer class for the Telegram bot.
 * Handles bot registration with Telegram API when the Spring context is refreshed.
 *
 * @author AI
 * @version 0.1-beta
 */
@Slf4j
@Component
public class BotInitializer {
    /** The bot instance to be registered */
    private final TelegramBot bot;

    /**
     * Creates a new BotInitializer instance.
     *
     * @param bot the bot instance to initialize
     */
    public BotInitializer(TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * Initializes and registers the bot with Telegram API.
     * This method is called automatically when the Spring context is refreshed.
     *
     * @throws TelegramApiException if there is an error registering the bot
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
} 