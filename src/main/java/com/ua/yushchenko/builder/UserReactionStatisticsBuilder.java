package com.ua.yushchenko.builder;

import static com.ua.yushchenko.model.ReactionType.BAD;
import static com.ua.yushchenko.model.ReactionType.FUNNY;
import static com.ua.yushchenko.model.ReactionType.SUPER;

import com.ua.yushchenko.model.domain.UserReactionStatistics;
import com.ua.yushchenko.service.reaction.ReactionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Builder for {@link UserReactionStatistics} entity.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Component
@RequiredArgsConstructor
public class UserReactionStatisticsBuilder {

    @NonNull
    private final ReactionService reactionService;

    /**
     * Build {@link UserReactionStatistics} by ID of chat and ID of prediction
     *
     * @param chatId       ID of chat
     * @param predictionId ID of prediction
     * @return {@link UserReactionStatistics}
     */
    public UserReactionStatistics build(final long chatId,
                                        final long predictionId) {

        final int superReactionCount = reactionService.getAllSuperReactions(predictionId).size();
        final int funnyReactionCount = reactionService.getAllFunnyReactions(predictionId).size();
        final int badReactionCount = reactionService.getAllBadReactions(predictionId).size();

        return UserReactionStatistics.builder()
                                     .superReactionCount(superReactionCount)
                                     .superReactionSelected(
                                             reactionService.getReactionByType(chatId, predictionId, SUPER)
                                                            .isPresent())
                                     .funnyReactionCount(funnyReactionCount)
                                     .funnyReactionSelected(
                                             reactionService.getReactionByType(chatId, predictionId, FUNNY)
                                                            .isPresent())
                                     .badReactionCount(badReactionCount)
                                     .badReactionSelected(
                                             reactionService.getReactionByType(chatId, predictionId, BAD)
                                                            .isPresent())
                                     .build();
    }

}
