package com.ua.yushchenko.service.reaction;

import java.util.List;
import java.util.Optional;

import com.ua.yushchenko.model.Reaction;
import com.ua.yushchenko.model.ReactionType;

/**
 * Service interface for managing reaction.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
public interface ReactionService {

    void addReaction(final long chatId, final long predictionId, final ReactionType reactionType);

    List<Reaction> getAllSuperReactions(final long predictionId);

    List<Reaction> getAllFunnyReactions(final long predictionId);

    List<Reaction> getAllBadReactions(final long predictionId);

    Optional<Reaction> getReactionByType(final long chatId, final long predictionId, final ReactionType reactionType);
}
