package com.ua.yushchenko.command;

import static com.ua.yushchenko.command.CommandConstants.BUTTON_ANOTHER_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_ANOTHER_DAILY;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_ANOTHER_PREDICTION;

import java.util.ArrayList;
import java.util.List;

import com.ua.yushchenko.service.telegram.MessageSender;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Base abstract class for callback commands triggered by inline keyboard buttons.
 * Extends BaseCommand to provide additional functionality specific to callback queries.
 * Handles message editing and inline keyboard updates.
 *
 * @author AI
 * @version 0.1-beta
 */
public abstract class AbstractCallbackCommand extends AbstractCommand {

    /**
     * ID of the message that triggered the callback
     */
    protected final int messageId;

    protected AbstractCallbackCommand(final MessageSender messageSender,
                                      final long chatId,
                                      final int messageId) {
        super(messageSender, chatId);
        this.messageId = messageId;
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
} 