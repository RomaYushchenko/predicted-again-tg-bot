package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Base abstract class for all bot commands.
 * Provides common functionality and fields used by all command implementations.
 * Implements the Command interface and handles basic message sending operations.
 *
 * @author AI
 * @version 0.1-beta
 */
public abstract class BaseCommand implements Command {
    /** The main bot instance used for sending messages */
    protected final TelegramBot bot;
    
    /** The chat ID where the command was invoked */
    protected final long chatId;
    
    /** Service for handling quick predictions */
    protected final PredictionService predictionService;
    
    /** Service for handling daily predictions and notifications */
    protected final DailyPredictionService dailyPredictionService;

    protected BaseCommand(TelegramBot bot, long chatId,
                         PredictionService predictionService,
                         DailyPredictionService dailyPredictionService) {
        this.bot = bot;
        this.chatId = chatId;
        this.predictionService = predictionService;
        this.dailyPredictionService = dailyPredictionService;
    }

    protected void sendMessage(String text) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    protected void sendMessage(String text, ReplyKeyboardMarkup replyMarkup) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .replyMarkup(replyMarkup)
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    protected void sendMessage(String text, InlineKeyboardMarkup replyMarkup) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .replyMarkup(replyMarkup)
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    protected void editMessage(int messageId, String text) {
        EditMessageText message = EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(text)
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    protected void editMessage(int messageId, String text, InlineKeyboardMarkup replyMarkup) {
        EditMessageText message = EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(text)
                .replyMarkup(replyMarkup)
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    protected void handleError(Exception e) {
        String errorMessage = "Сталася помилка. Спробуйте ще раз пізніше.";
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(errorMessage)
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException ex) {
            // Якщо не вдалося відправити повідомлення про помилку, просто логуємо її
            // Не намагаємось відправити ще одне повідомлення, щоб уникнути рекурсії
            System.err.println("Failed to send error message: " + ex.getMessage());
            System.err.println("Original error: " + e.getMessage());
        }
    }

    protected void handleMessageNotModified() {
        // Override this method in specific commands if needed
    }
} 