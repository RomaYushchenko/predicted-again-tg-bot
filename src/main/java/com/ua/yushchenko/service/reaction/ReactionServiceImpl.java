package com.ua.yushchenko.service.reaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ua.yushchenko.model.Reaction;
import com.ua.yushchenko.model.ReactionType;
import com.ua.yushchenko.repository.ReactionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Class that represented logic to reaction
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    @NonNull
    private final ReactionRepository reactionRepository;

    @Override
    public void addReaction(final long chatId, final long predictionId, final ReactionType reactionType) {

        final Optional<Reaction> reaction = getReactionByType(chatId, predictionId, reactionType);

        if (reaction.isPresent()) {
            reactionRepository.deleteById(reaction.get().getId());
            return;
        }

        final Reaction reactionToCreate = new Reaction();
        reactionToCreate.setChatId(chatId);
        reactionToCreate.setPredictionId(predictionId);
        reactionToCreate.setReactionType(reactionType);
        reactionToCreate.setCreatedAt(LocalDateTime.now());

        reactionRepository.save(reactionToCreate);
        return;
    }

    @Override
    public List<Reaction> getAllSuperReactions(final long predictionId) {
        return reactionRepository.findReactionsByPredictionIdAndReactionType(predictionId, ReactionType.SUPER);
    }

    @Override
    public List<Reaction> getAllFunnyReactions(final long predictionId) {
        return reactionRepository.findReactionsByPredictionIdAndReactionType(predictionId, ReactionType.FUNNY);
    }

    @Override
    public List<Reaction> getAllBadReactions(final long predictionId) {
        return reactionRepository.findReactionsByPredictionIdAndReactionType(predictionId, ReactionType.BAD);
    }

    @Override
    public Optional<Reaction> getReactionByType(final long chatId, final long predictionId,
                                                final ReactionType reactionType) {
        return reactionRepository.findReactionByChatIdAndPredictionIdAndReactionType(chatId, predictionId,
                                                                                     reactionType);
    }
}
