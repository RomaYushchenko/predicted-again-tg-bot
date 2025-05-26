package com.ua.yushchenko.builder.ui.magicball;

import static com.ua.yushchenko.command.CommandConstants.BUTTON_BACK_TO_MAIN_MENU;
import static com.ua.yushchenko.command.CommandConstants.BUTTON_SETTINGS;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_BACK_TO_MAIN_MENU;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_SETTINGS;

import java.util.ArrayList;
import java.util.List;

import com.ua.yushchenko.builder.ui.ButtonBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Builder that provide logic to build button for Magic Ball command.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Component
@RequiredArgsConstructor
public class MagicBallButtonBuilder {

    @NonNull
    private final ButtonBuilder buttonBuilder;

    /**
     * Build back to main menu buttons Keyboard
     *
     * @return {@link InlineKeyboardMarkup} with MagicBall buttons
     */
    public InlineKeyboardMarkup buildBackKeyboard() {
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        final List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(buttonBuilder.button(BUTTON_BACK_TO_MAIN_MENU, CALLBACK_BACK_TO_MAIN_MENU));

        rowsInline.add(row1);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }
}
