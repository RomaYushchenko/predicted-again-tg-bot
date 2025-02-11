package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.ua.yushchenko.command.CommandConstants.*;

public abstract class BaseMessageCommand extends BaseCommand {

    protected BaseMessageCommand(TelegramBot bot, long chatId,
                               PredictionService predictionService,
                               DailyPredictionService dailyPredictionService) {
        super(bot, chatId, predictionService, dailyPredictionService);
    }

    protected void sendMessage(String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
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
                .chatId(chatId)
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
                .chatId(chatId)
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

    protected InlineKeyboardMarkup createSettingsInlineKeyboard(boolean notificationsEnabled) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton toggleButton = new InlineKeyboardButton();
        toggleButton.setText(notificationsEnabled ? BUTTON_DISABLE_NOTIFICATIONS : BUTTON_ENABLE_NOTIFICATIONS);
        toggleButton.setCallbackData(CALLBACK_TOGGLE_NOTIFICATIONS);
        row1.add(toggleButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton timeButton = new InlineKeyboardButton();
        timeButton.setText(BUTTON_CHANGE_TIME);
        timeButton.setCallbackData(CALLBACK_CHANGE_TIME);
        row2.add(timeButton);

        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    protected InlineKeyboardMarkup createBackToSettingsInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton menuButton = new InlineKeyboardButton();
        menuButton.setText("⚙️ До налаштувань");
        menuButton.setCallbackData("settings");
        rowInline.add(menuButton);

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    protected void showMainMenu() {
        sendMessage("Головне меню:", createMainMenuKeyboard());
    }
} 