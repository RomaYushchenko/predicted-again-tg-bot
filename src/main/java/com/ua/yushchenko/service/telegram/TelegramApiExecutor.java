package com.ua.yushchenko.service.telegram;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

/**
 * Interface for executing Telegram API methods.
 * Provides a generic way to execute any Telegram Bot API method and handle responses.
 *
 * @author AI
 * @version 0.1-beta
 */
public interface TelegramApiExecutor {
    /**
     * Executes a Telegram API method and returns its result.
     *
     * @param method the method to execute
     * @param <T> the type of the response
     * @param <Method> the type of the method
     * @return the result of the method execution
     * @throws TelegramApiException if there is an error executing the method
     */
    <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException;
} 