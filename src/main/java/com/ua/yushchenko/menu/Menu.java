package com.ua.yushchenko.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Interface for creating Telegram bot menus.
 * Provides a common contract for different types of menus (inline and reply keyboards).
 *
 * @author AI
 * @version 0.1-beta
 */
public interface Menu {
    /**
     * Creates and returns a keyboard markup for the menu.
     *
     * @return ReplyKeyboard implementation (can be either InlineKeyboardMarkup or ReplyKeyboardMarkup)
     */
    ReplyKeyboard create();
} 