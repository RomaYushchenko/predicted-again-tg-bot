package com.ua.yushchenko.builder.ui;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Common builder for {@link InlineKeyboardButton}.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Component
public class ButtonBuilder {

    /**
     * Build {@link InlineKeyboardButton}
     *
     * @param text         message
     * @param callbackData callback data
     * @return {@link InlineKeyboardButton}
     */
    public InlineKeyboardButton button(final String text, final String callbackData) {
        final InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }
}
