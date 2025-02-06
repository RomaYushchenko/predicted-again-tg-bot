package com.ua.yushchenko.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for reply keyboard menus in the Telegram bot.
 * Provides common functionality for creating reply keyboard markups.
 *
 * @author AI
 * @version 0.1-beta
 */
public abstract class AbstractReplyMenu implements Menu {
    protected final List<KeyboardRow> rows = new ArrayList<>();

    /**
     * Adds a button to the menu with specified text.
     *
     * @param text text to display on the button
     */
    protected void addButton(String text) {
        KeyboardRow row = new KeyboardRow();
        row.add(text);
        rows.add(row);
    }

    /**
     * Creates and returns a reply keyboard markup with all added buttons.
     * The keyboard is configured to be resizable, persistent, and selective.
     *
     * @return ReplyKeyboardMarkup containing all added buttons
     */
    @Override
    public ReplyKeyboardMarkup create() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        markup.setSelective(true);
        return markup;
    }
} 