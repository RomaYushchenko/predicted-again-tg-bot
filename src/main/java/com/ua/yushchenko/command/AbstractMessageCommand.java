package com.ua.yushchenko.command;

import com.ua.yushchenko.service.telegram.MessageSender;

public abstract class AbstractMessageCommand extends AbstractCommand {

    protected AbstractMessageCommand(MessageSender messageSender, long chatId) {
        super(messageSender, chatId);
    }
} 