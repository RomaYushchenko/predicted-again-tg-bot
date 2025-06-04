package com.ua.yushchenko.service.telegram;

import java.io.Serializable;

import com.ua.yushchenko.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
     * @param method   the method to execute
     * @param <T>      the type of the response
     * @param <Method> the type of the method
     * @return the result of the method execution
     * @throws TelegramApiException if there is an error executing the method
     */
    @Retryable(
            value = { TelegramApiException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000) // 2 секунди між спробами
    )
    @Async
    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method method) throws
            TelegramApiException {
        try {
            bot.execute(method);
        } catch (TelegramApiException e) {
            if (e.getMessage().contains("message is not modified")) {
                log.debug("Message is not modified");
                throw e;
            }
            log.error("Error executing method: {}", e.getMessage());
            throw e;
        }
    }

    @Recover
    public void recover(TelegramApiException e, Long chatId, String messageText) {
        log.warn("🔁 All retries failed for chatId {}. Message not sent: {}", chatId, messageText);
    }
} 