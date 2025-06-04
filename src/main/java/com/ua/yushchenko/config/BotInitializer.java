
package com.ua.yushchenko.config;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.config.BotConfig.BotSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Configuration
@RequiredArgsConstructor
public class BotInitializer {
    /** The bot instance to be registered */
    private final TelegramBot bot;
    private final BotSettings botSettings;


    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botSettings.getWebhookUrl()).build();
    }

    /**
     * Initializes and registers the bot with Telegram API.
     * This method is called automatically when the Spring context is refreshed.
     *
     * @throws TelegramApiException if there is an error registering the bot
     */
    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            SetWebhook setWebhook = SetWebhook.builder()
                                             .url(botSettings.getWebhookUrl())
                                             .build();
            bot.setWebhook(setWebhook);
            telegramBotsApi.registerBot(bot, setWebhook);
            log.info("Webhook registered at {}", botSettings.getWebhookUrl());
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }

        return telegramBotsApi;
    }
}
