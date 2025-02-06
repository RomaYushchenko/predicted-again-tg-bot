package com.ua.yushchenko.menu;

import com.ua.yushchenko.service.DailyPredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.ua.yushchenko.command.CommandConstants.*;

/**
 * Factory class for creating different types of Telegram bot menus.
 * Responsible for generating main menu and settings menu with appropriate buttons and layouts.
 *
 * @author AI
 * @version 0.1-beta
 */
@Component
@RequiredArgsConstructor
public class MenuFactory {
    private final DailyPredictionService dailyPredictionService;

    /**
     * Creates the main menu of the bot with quick prediction and daily prediction buttons.
     *
     * @return ReplyKeyboard containing the main menu buttons
     */
    public ReplyKeyboard createMainMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(COMMAND_QUICK_PREDICTION));
        row1.add(new KeyboardButton(COMMAND_DAILY_PREDICTION));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(COMMAND_SETTINGS_BUTTON));

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public ReplyKeyboard createPredictionMenu() {
        return new PredictionMenu().create();
    }

    public ReplyKeyboard createDailyPredictionMenu() {
        return new DailyPredictionMenu().create();
    }

    /**
     * Creates the settings menu for a specific chat.
     * Includes buttons for changing notification time and toggling notifications.
     *
     * @param chatId ID of the chat to create settings menu for
     * @return ReplyKeyboard containing the settings menu buttons
     */
    public ReplyKeyboard createSettingsMenu(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(BUTTON_CHANGE_TIME));
        row1.add(new KeyboardButton(dailyPredictionService.isNotificationsEnabled(chatId) ? 
            BUTTON_DISABLE_NOTIFICATIONS : BUTTON_ENABLE_NOTIFICATIONS));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(BUTTON_BACK_TO_MENU));

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
} 