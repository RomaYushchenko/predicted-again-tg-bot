package com.ua.yushchenko.builder.ui.prediction;

import static com.ua.yushchenko.command.CommandConstants.BUTTON_ANOTHER_PREDICTION;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_ANOTHER_PREDICTION;

import java.util.ArrayList;
import java.util.List;

import com.ua.yushchenko.builder.ui.ButtonBuilder;
import com.ua.yushchenko.builder.ui.reaction.ReactionButtonBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Builder that provide logic to build button for quick prediction.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Component
@RequiredArgsConstructor
public class QuickPredictionButtonBuilder {

    @NonNull
    private final ButtonBuilder buttonBuilder;
    @NonNull
    private final ReactionButtonBuilder reactionButtonBuilder;

    /**
     * Build Quick Prediction buttons keyboard
     *
     * @param predictionId ID of prediction
     * @param firstPrefix  prefix
     * @return {@link InlineKeyboardMarkup} with Quick Prediction buttons
     */
    public InlineKeyboardMarkup buildKeyboard(final long predictionId, final String firstPrefix) {
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        rowsInline.add(reactionButtonBuilder.buildKeyboardRow(predictionId, firstPrefix));
        rowsInline.add(buildKeyboardRow());

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    /**
     * Build Quick Prediction buttons keyboard wth callback reactions
     *
     * @param chatId       ID of chat
     * @param predictionId ID of prediction
     * @param firstPrefix  prefix
     * @return {@link InlineKeyboardMarkup} with Quick Prediction buttons
     */
    public InlineKeyboardMarkup buildCallbackKeyboard(final long chatId, final long predictionId,
                                                      final String firstPrefix) {
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        rowsInline.add(reactionButtonBuilder.buildCallbackKeyboardRow(chatId, predictionId, firstPrefix));
        rowsInline.add(buildKeyboardRow());

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private List<InlineKeyboardButton> buildKeyboardRow() {
        final List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(buttonBuilder.button(BUTTON_ANOTHER_PREDICTION, CALLBACK_ANOTHER_PREDICTION));
        return row;
    }
}
