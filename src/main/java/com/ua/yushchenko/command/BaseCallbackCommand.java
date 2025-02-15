package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.ua.yushchenko.command.CommandConstants.*;

/**
 * Base abstract class for callback commands triggered by inline keyboard buttons.
 * Extends BaseCommand to provide additional functionality specific to callback queries.
 * Handles message editing and inline keyboard updates.
 *
 * @author AI
 * @version 0.1-beta
 */
public abstract class BaseCallbackCommand extends BaseCommand {
    /** ID of the message that triggered the callback */
    protected final int messageId;

    protected BaseCallbackCommand(TelegramBot bot, long chatId, int messageId,
                                PredictionService predictionService,
                                DailyPredictionService dailyPredictionService) {
        super(bot, chatId, predictionService, dailyPredictionService);
        this.messageId = messageId;
    }

    protected void editMessage(String text) {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    protected void editMessage(String text, InlineKeyboardMarkup replyMarkup) {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
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

    protected ReplyKeyboardMarkup createMainMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(COMMAND_QUICK_PREDICTION);
        row1.add(COMMAND_DAILY_PREDICTION);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(COMMAND_SETTINGS_BUTTON);

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    protected InlineKeyboardMarkup createPredictionInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton anotherButton = new InlineKeyboardButton();
        anotherButton.setText(BUTTON_ANOTHER_PREDICTION);
        anotherButton.setCallbackData(CALLBACK_ANOTHER_PREDICTION);

        rowInline.add(anotherButton);

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    protected InlineKeyboardMarkup createDailyPredictionInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton anotherButton = new InlineKeyboardButton();
        anotherButton.setText(BUTTON_ANOTHER_PREDICTION);
        anotherButton.setCallbackData(CALLBACK_ANOTHER_DAILY);

        rowInline.add(anotherButton);

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }
} 