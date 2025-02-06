package com.ua.yushchenko.service.telegram;

import com.ua.yushchenko.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

/**
 * Implementation of TelegramApiExecutor interface.
 * Executes Telegram API methods through the bot instance and provides error handling and logging.
 *
 * @author AI
 * @version 0.1-beta
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramApiExecutorImpl implements TelegramApiExecutor {
    private final TelegramBot bot;

    /**
     * Executes a Telegram API method and handles any errors that occur.
     * Logs errors and rethrows exceptions for proper error handling up the chain.
     *
     * @param method the method to execute
     * @param <T> the type of the response
     * @param <Method> the type of the method
     * @return the result of the method execution
     * @throws TelegramApiException if there is an error executing the method
     */
    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        try {
            return bot.execute(method);
        } catch (TelegramApiException e) {
            log.error("Failed to execute Telegram API method: {}", e.getMessage());
            throw e;
        }
    }
} 