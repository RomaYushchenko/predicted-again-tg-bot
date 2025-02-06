package com.ua.yushchenko.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for inline keyboard menus in the Telegram bot.
 * Provides common functionality for creating inline keyboard markups.
 *
 * @author AI
 * @version 0.1-beta
 */
public abstract class AbstractInlineMenu implements Menu {
    protected final List<List<InlineKeyboardButton>> rows = new ArrayList<>();

    /**
     * Adds a button to the menu with specified text and callback data.
     *
     * @param text text to display on the button
     * @param callbackData data to be sent when the button is pressed
     */
    protected void addButton(String text, String callbackData) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        rows.add(List.of(button));
    }

    /**
     * Creates and returns an inline keyboard markup with all added buttons.
     *
     * @return InlineKeyboardMarkup containing all added buttons
     */
    @Override
    public InlineKeyboardMarkup create() {
        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }
} 