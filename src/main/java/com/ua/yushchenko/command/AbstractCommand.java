package com.ua.yushchenko.command;

import com.ua.yushchenko.service.telegram.MessageSender;

/**
 * Base abstract class for all bot commands.
 * Provides common functionality and fields used by all command implementations.
 * Implements the Command interface and handles basic message sending operations.
 *
 * @author AI
 * @version 0.1-beta
 */
public abstract class AbstractCommand implements Command {

    /**
     * The main bot instance used for sending messages
     */
    protected final MessageSender messageSender;

    /**
     * The chat ID where the command was invoked
     */
    protected final long chatId;

    protected AbstractCommand(final MessageSender messageSender,
                              final long chatId) {
        this.messageSender = messageSender;
        this.chatId = chatId;
    }
} 