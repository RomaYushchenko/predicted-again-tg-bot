package com.ua.yushchenko.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Telegram bot settings.
 * Uses environment variables from .env file for bot credentials.
 *
 * @author AI
 * @version 0.1-beta
 */
@Configuration
public class BotConfig {

    /**
     * Creates and configures bot settings using environment variables.
     *
     * @param dotenv Dotenv instance for accessing environment variables
     * @return configured bot settings
     */
    @Bean
    public BotSettings botSettings(Dotenv dotenv) {
        BotSettings settings = new BotSettings();
        settings.setName(dotenv.get("BOT_NAME"));
        settings.setToken(dotenv.get("BOT_TOKEN"));
        settings.setWebhookPath(dotenv.get("WEBHOOK_PATH"));
        settings.setWebhookUrl(dotenv.get("WEBHOOK_URL"));
        return settings;
    }

    /**
     * Inner class for holding bot settings.
     */
    @Getter
    @Setter
    public static class BotSettings {
        private String name;
        private String token;
        private String webhookPath;
        private String webhookUrl;
    }
}
