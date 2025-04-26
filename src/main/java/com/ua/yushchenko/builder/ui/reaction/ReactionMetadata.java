package com.ua.yushchenko.builder.ui.reaction;

import com.ua.yushchenko.model.ReactionType;

/**
 * Class that keep metadata of reaction.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
public record ReactionMetadata(ReactionType type, boolean isSelected, int count, String callbackPrefix) {

}
