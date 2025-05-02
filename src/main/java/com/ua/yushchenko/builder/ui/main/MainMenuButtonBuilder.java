package com.ua.yushchenko.builder.ui.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ua.yushchenko.model.MainMenuButton;
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
     *
     * @return {@link ReplyKeyboardMarkup} with Main Menu buttons
     */
    public ReplyKeyboardMarkup build(final List<MainMenuButton> mainMenuButtons) {
        final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        Map<Integer, List<MainMenuButton>> groupedByRow =
                mainMenuButtons.stream()
                               .collect(Collectors.groupingBy(MainMenuButton::getRowNumber));

        List<KeyboardRow> keyboard = new ArrayList<>();

        groupedByRow.keySet()
                    .stream()
                    .sorted()
                    .forEach(rowNumber -> {
                        KeyboardRow row = new KeyboardRow();
                        groupedByRow.get(rowNumber)
                                    .forEach(button -> row.add(button.getButtonLabel()));
                        keyboard.add(row);
                    });

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
