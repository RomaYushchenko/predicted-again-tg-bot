package com.ua.yushchenko.model.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Entity class representing statistics of user reaction.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Data
@Builder(toBuilder = true)
public class UserReactionStatistics {

    int superReactionCount;
    int funnyReactionCount;
    int badReactionCount;

    boolean superReactionSelected;
    boolean funnyReactionSelected;
    boolean badReactionSelected;
}
