package com.ua.yushchenko.model;

import lombok.Getter;

/**
 * Enum of type of reaction
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Getter
public enum ReactionType {

    SUPER("\uD83C\uDFAF"), // 🎯
    FUNNY("\uD83E\uDD2A"), // 🤪
    BAD("\uD83D\uDE48");   // 🙈

    private final String emoji;

    ReactionType(String emoji) {
        this.emoji = emoji;
    }

}
