package com.ua.yushchenko.service.telegram;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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
     * @param chatId  the ID of the chat to send the message to
     * @param message the message to send
     * @throws TelegramApiException if there is an error sending the message
     */
    void sendMessage(long chatId, String message) throws TelegramApiException;

    void sendMessage(long chatId, String text, ReplyKeyboard replyMarkup) throws TelegramApiException;

    void sendMessage(long chatId, String text, ReplyKeyboardMarkup replyMarkup) throws TelegramApiException;

    void sendMessage(final BotApiMethod<?> method) throws TelegramApiException;

    void editMessage(long chatId, int messageId, String message) throws TelegramApiException;

    void editMessage(long chatId, int messageId, String text, InlineKeyboardMarkup replyMarkup) throws
            TelegramApiException;
} 