package com.ua.yushchenko.builder.ui.main;

import static com.ua.yushchenko.command.CommandConstants.COMMAND_DAILY_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_QUICK_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.COMMAND_SETTINGS_BUTTON;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Builder that provide logic to build button for main menu.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Component
@RequiredArgsConstructor
public class MainMenuButtonBuilder {

    /**
     * Build Main Menu buttons keyboard
     * @return {@link ReplyKeyboardMarkup} with Main Menu buttons
     */
    public ReplyKeyboardMarkup build() {
        final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        final List<KeyboardRow> keyboard = new ArrayList<>();

        final KeyboardRow row1 = new KeyboardRow();
        row1.add(COMMAND_QUICK_PREDICTION);
        row1.add(COMMAND_DAILY_PREDICTION);

        final KeyboardRow row2 = new KeyboardRow();
        row2.add(COMMAND_SETTINGS_BUTTON);

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
