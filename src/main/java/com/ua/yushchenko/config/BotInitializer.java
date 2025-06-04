
package com.ua.yushchenko.config;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.config.BotConfig.BotSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
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
    private final BotSettings botSettings;

    /**
     * Creates a new BotInitializer instance.
     *
     * @param bot the bot instance to initialize
     */
    public BotInitializer(TelegramBot bot, BotSettings botSettings) {
        this.bot = bot;
        this.botSettings = botSettings;
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
            SetWebhook setWebhook = SetWebhook.builder()
                                             .url(botSettings.getWebhookUrl())
                                             .build();
            telegramBotsApi.registerBot(bot, setWebhook);
            log.info("Webhook registered at {}", botSettings.getWebhookUrl());
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
