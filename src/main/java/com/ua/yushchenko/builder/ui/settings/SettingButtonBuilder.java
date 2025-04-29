package com.ua.yushchenko.builder.ui.settings;

import static com.ua.yushchenko.command.CommandConstants.BUTTON_CHANGE_TIME;
import static com.ua.yushchenko.command.CommandConstants.BUTTON_DISABLE_NOTIFICATIONS;
import static com.ua.yushchenko.command.CommandConstants.BUTTON_ENABLE_NOTIFICATIONS;
import static com.ua.yushchenko.command.CommandConstants.BUTTON_SETTINGS;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_CHANGE_TIME;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_SETTINGS;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_TOGGLE_NOTIFICATIONS;

import java.util.ArrayList;
import java.util.List;

import com.ua.yushchenko.builder.ui.ButtonBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Builder that provide logic to build button for setting.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Component
@RequiredArgsConstructor
public class SettingButtonBuilder {

    @NonNull
    private final ButtonBuilder buttonBuilder;

    /**
     * Build setting buttons Keyboard
     *
     * @param notificationsEnabled notification is
     * @return {@link InlineKeyboardMarkup} with setting buttons
     */
    public InlineKeyboardMarkup buildKeyboard(final boolean notificationsEnabled ) {
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        final List<InlineKeyboardButton> row1 = new ArrayList<>();
        final List<InlineKeyboardButton> row2 = new ArrayList<>();

        row1.add(buttonBuilder.button(notificationsEnabled
                         ? BUTTON_DISABLE_NOTIFICATIONS
                         : BUTTON_ENABLE_NOTIFICATIONS,
                 CALLBACK_TOGGLE_NOTIFICATIONS));
        row2.add(buttonBuilder.button(BUTTON_CHANGE_TIME, CALLBACK_CHANGE_TIME));

        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    /**
     * Build back to setting buttons Keyboard
     *
     * @return {@link InlineKeyboardMarkup} with setting buttons
     */
    public InlineKeyboardMarkup buildBackKeyboard() {
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        final List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(buttonBuilder.button(BUTTON_SETTINGS, CALLBACK_SETTINGS));

        rowsInline.add(row1);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }
}
