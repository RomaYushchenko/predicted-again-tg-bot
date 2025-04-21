package com.ua.yushchenko.command;

import com.ua.yushchenko.service.telegram.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UnknownCommand extends AbstractMessageCommand {

    public UnknownCommand(final MessageSender messageSender,
                          final long chatId) {
        super(messageSender, chatId);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String message = "На жаль, я не розумію цю команду. Будь ласка, використовуйте кнопки меню.";
        messageSender.sendMessage(chatId, message, createMainMenuKeyboard());
    }

    @Override
    public String getCommandName() {
        return "unknown";
    }

    @Override
    public String getDescription() {
        return "Невідома команда";
    }
} 