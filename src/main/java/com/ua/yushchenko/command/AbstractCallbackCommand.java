package com.ua.yushchenko.command;

import com.ua.yushchenko.service.telegram.MessageSender;

/**
 * Base abstract class for callback commands triggered by inline keyboard buttons.
 * Extends BaseCommand to provide additional functionality specific to callback queries.
 * Handles message editing and inline keyboard updates.
 *
 * @author AI
 * @version 0.1-beta
 */
public abstract class AbstractCallbackCommand extends AbstractCommand {

    /**
     * ID of the message that triggered the callback
     */
    protected final int messageId;

    protected AbstractCallbackCommand(final MessageSender messageSender,
                                      final long chatId,
                                      final int messageId) {
        super(messageSender, chatId);
        this.messageId = messageId;
    }
} 