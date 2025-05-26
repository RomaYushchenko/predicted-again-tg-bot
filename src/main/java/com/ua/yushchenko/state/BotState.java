package com.ua.yushchenko.state;

/**
 * Represents the possible states of the Telegram bot.
 * Used to track the current state of user interaction with the bot.
 *
 * @author AI
 * @version 0.1-beta
 */
public enum BotState {
    /**
     * Default state of the bot. Used when no specific action is expected from the user.
     */
    DEFAULT,
    
    /**
     * State indicating that the bot is waiting for the user to input a time for notifications.
     */
    AWAITING_TIME,

    /**
     * State indicating that the bot is waiting for the user to input a question.
     */
    AWAITING_QUESTION,
} 