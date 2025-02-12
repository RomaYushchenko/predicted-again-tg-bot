package com.ua.yushchenko.service.telegram;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Service for interacting with Telegram API.
 * Responsible for sending messages and handling user interactions.
 *
 * @author AI
 * @version 0.1-beta
 */
public interface TelegramBotService {
    /**
     * Sends a text message to a chat.
     *
     * @param chatId ID of the chat
     * @param text message text
     * @throws TelegramApiException if there is an error sending the message
     */
    void sendMessage(long chatId, String text) throws TelegramApiException;

    /**
     * Sends a message with an inline keyboard.
     *
     * @param chatId ID of the chat
     * @param text message text
     * @param replyMarkup inline keyboard markup
     * @throws TelegramApiException if there is an error sending the message
     */
    void sendMessage(long chatId, String text, InlineKeyboardMarkup replyMarkup) throws TelegramApiException;
} 