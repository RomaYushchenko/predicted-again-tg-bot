package com.ua.yushchenko.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Interface for sending messages.
 * Provides abstraction for message sending functionality.
 *
 * @author AI
 * @version 0.1-beta
 */
public interface MessageSender {
    /**
     * Sends a message to the specified chat.
     *
     * @param chatId the ID of the chat to send the message to
     * @param message the message to send
     * @throws TelegramApiException if there is an error sending the message
     */
    void sendMessage(long chatId, String message) throws TelegramApiException;
} 