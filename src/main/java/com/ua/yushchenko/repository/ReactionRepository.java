package com.ua.yushchenko.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ua.yushchenko.model.Reaction;
import com.ua.yushchenko.model.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Reaction entity operations.
 * Provides methods for querying and managing Reaction data in the database.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, UUID> {

    Optional<Reaction> findReactionByChatIdAndPredictionIdAndReactionType(final long chatId,
                                                                          final long predictionId,
                                                                          final ReactionType reactionType);

    List<Reaction> findReactionsByPredictionIdAndReactionType(final long predictionId, final ReactionType reactionType);
}
