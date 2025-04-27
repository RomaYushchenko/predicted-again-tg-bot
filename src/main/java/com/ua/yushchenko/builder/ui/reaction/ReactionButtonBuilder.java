package com.ua.yushchenko.builder.ui.reaction;

import static com.ua.yushchenko.command.CommandConstants.CALLBACK_REACTION_BAD;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_REACTION_FUNNY;
import static com.ua.yushchenko.command.CommandConstants.CALLBACK_REACTION_SUPER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ua.yushchenko.builder.UserReactionStatisticsBuilder;
import com.ua.yushchenko.builder.ui.ButtonBuilder;
import com.ua.yushchenko.model.ReactionType;
import com.ua.yushchenko.model.domain.UserReactionStatistics;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Builder that provide logic to build button for user reaction.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Component
@RequiredArgsConstructor
public class ReactionButtonBuilder {

    @NonNull
    private final ButtonBuilder buttonBuilder;
    @NonNull
    private final UserReactionStatisticsBuilder userReactionStatisticsBuilder;

    /**
     * Build reaction buttons Keyboard
     *
     * @param predictionId ID of prediction
     * @param firstPrefix  prefix
     * @return {@link InlineKeyboardMarkup} with reaction buttons
     */
    public InlineKeyboardMarkup buildKeyboard(final long chatId, final long predictionId, final String firstPrefix) {
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        rowsInline.add(buildKeyboardRow(chatId, predictionId, firstPrefix));

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    /**
     * Build reaction buttons row
     *
     * @param predictionId ID of prediction
     * @param firstPrefix  prefix
     * @return List of {@link InlineKeyboardButton} with reaction buttons
     */
    public List<InlineKeyboardButton> buildKeyboardRow(final long chatId,
                                                       final long predictionId,
                                                       final String firstPrefix) {

        final List<InlineKeyboardButton> row1 = new ArrayList<>();

        final UserReactionStatistics reactionStatistics = userReactionStatisticsBuilder.build(chatId, predictionId);

        final var reactionMetadata = Arrays.stream(ReactionType.values())
                                            .map(reactionType -> createReactionMetadata(reactionType,
                                                                                        reactionStatistics))
                                            .toList();

        if (reactionMetadata.stream().anyMatch(ReactionMetadata::isSelected)) {
            for (final ReactionMetadata metadata : reactionMetadata) {
                row1.add(buildReactionButton(metadata, firstPrefix, predictionId));
            }
        } else {
            row1.add(buttonBuilder.button(ReactionType.SUPER.getEmoji(),
                                          firstPrefix + CALLBACK_REACTION_SUPER + predictionId));
            row1.add(buttonBuilder.button(ReactionType.FUNNY.getEmoji(),
                                          firstPrefix + CALLBACK_REACTION_FUNNY + predictionId));
            row1.add(buttonBuilder.button(ReactionType.BAD.getEmoji(),
                                          firstPrefix + CALLBACK_REACTION_BAD + predictionId));
        }

        return row1;
    }

    /**
     * Build reaction buttons wth callback reactions
     *
     * @param chatId       ID of chat
     * @param predictionId ID of prediction
     * @param firstPrefix  prefix
     * @return {@link InlineKeyboardMarkup} with reaction buttons
     */
//    public InlineKeyboardMarkup buildCallbackKeyboard(final long chatId,
//                                                      final long predictionId,
//                                                      final String firstPrefix) {
//        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//
//        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        rowsInline.add(buildCallbackKeyboardRow(chatId, predictionId, firstPrefix));
//
//        markupInline.setKeyboard(rowsInline);
//        return markupInline;
//    }

    /**
     * Build reaction buttons row  wth callback reactions
     *
     * @param chatId       ID of chat
     * @param predictionId ID of prediction
     * @param firstPrefix  prefix
     * @return List of {@link InlineKeyboardButton} with reaction buttons
     */
//    public List<InlineKeyboardButton> buildCallbackKeyboardRow(final long chatId,
//                                                               final long predictionId,
//                                                               final String firstPrefix) {
//        final List<InlineKeyboardButton> row1 = new ArrayList<>();
//
//        final UserReactionStatistics reactionStatistics = userReactionStatisticsBuilder.build(chatId, predictionId);
//
//        row1.add(buildReactionButton(createReactionMetadata(ReactionType.SUPER, reactionStatistics), firstPrefix,
//                                     predictionId));
//        row1.add(buildReactionButton(createReactionMetadata(ReactionType.FUNNY, reactionStatistics), firstPrefix,
//                                     predictionId));
//        row1.add(buildReactionButton(createReactionMetadata(ReactionType.BAD, reactionStatistics), firstPrefix,
//                                     predictionId));
//
//        return row1;
//    }

    private InlineKeyboardButton buildReactionButton(final ReactionMetadata metadata, final String firstPrefix,
                                                     final long predictionId) {
        final StringBuilder buttonText = new StringBuilder();

        if (metadata.isSelected()) {
            buttonText.append("✅ ");
        }

        buttonText.append(metadata.type().getEmoji())
                  .append(" (")
                  .append(metadata.count())
                  .append(")");

        return buttonBuilder.button(buttonText.toString(), firstPrefix + metadata.callbackPrefix() + predictionId);
    }

    private ReactionMetadata createReactionMetadata(final ReactionType type, final UserReactionStatistics statistics) {
        final boolean isSelected = switch (type) {
            case SUPER -> statistics.isSuperReactionSelected();
            case FUNNY -> statistics.isFunnyReactionSelected();
            case BAD -> statistics.isBadReactionSelected();
        };

        final int count = switch (type) {
            case SUPER -> statistics.getSuperReactionCount();
            case FUNNY -> statistics.getFunnyReactionCount();
            case BAD -> statistics.getBadReactionCount();
        };

        final String callbackPrefix = switch (type) {
            case SUPER -> CALLBACK_REACTION_SUPER;
            case FUNNY -> CALLBACK_REACTION_FUNNY;
            case BAD -> CALLBACK_REACTION_BAD;
        };

        return new ReactionMetadata(type, isSelected, count, callbackPrefix);
    }
}
