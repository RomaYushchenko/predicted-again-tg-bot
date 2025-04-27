package com.ua.yushchenko.builder.ui.prediction;

import java.util.ArrayList;
import java.util.List;

import com.ua.yushchenko.builder.ui.reaction.ReactionButtonBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Builder that provide logic to build button for daily prediction.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Component
@RequiredArgsConstructor
public class DailyPredictionButtonBuilder {

    @NonNull
    private final ReactionButtonBuilder reactionButtonBuilder;

    /**
     * Build Quick Prediction buttons keyboard
     *
     * @param predictionId ID of prediction
     * @param firstPrefix  prefix
     * @return {@link InlineKeyboardMarkup} with Quick Prediction buttons
     */
    public InlineKeyboardMarkup buildKeyboard(final long chatId, final Long predictionId, final String firstPrefix) {
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        rowsInline.add(reactionButtonBuilder.buildKeyboardRow(chatId, predictionId, firstPrefix));

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
