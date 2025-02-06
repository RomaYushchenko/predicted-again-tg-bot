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
     * Sends a message with a keyboard markup.
     *
     * @param chatId ID of the chat
     * @param text message text
     * @param keyboard keyboard markup
     * @throws TelegramApiException if there is an error sending the message
     */
    void sendMessageWithKeyboard(long chatId, String text, ReplyKeyboard keyboard) throws TelegramApiException;

    /**
     * Edits an existing message.
     *
     * @param chatId ID of the chat
     * @param messageId ID of the message to edit
     * @param text new message text
     * @throws TelegramApiException if there is an error editing the message
     */
    void editMessage(long chatId, int messageId, String text) throws TelegramApiException;

    /**
     * Edits a message with an inline keyboard.
     *
     * @param chatId ID of the chat
     * @param messageId ID of the message to edit
     * @param text new message text
     * @param keyboard new inline keyboard markup
     * @throws TelegramApiException if there is an error editing the message
     */
    void editMessage(long chatId, int messageId, String text, InlineKeyboardMarkup keyboard) throws TelegramApiException;

    /**
     * Shows the main menu.
     *
     * @param chatId ID of the chat
     * @throws TelegramApiException if there is an error showing the menu
     */
    void showMainMenu(long chatId) throws TelegramApiException;

    /**
     * Shows the settings menu.
     *
     * @param chatId ID of the chat
     * @throws TelegramApiException if there is an error showing the settings
     */
    void showSettings(long chatId) throws TelegramApiException;

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