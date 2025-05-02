package com.ua.yushchenko.command;

import com.ua.yushchenko.builder.ui.main.MainMenuButtonBuilder;
import com.ua.yushchenko.service.mainmenubutton.MainMenuButtonService;
import com.ua.yushchenko.service.telegram.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UnknownCommand extends AbstractMessageCommand {

    private final MainMenuButtonBuilder mainMenuButtonBuilder;
    private final MainMenuButtonService mainMenuButtonService;

    public UnknownCommand(final MessageSender messageSender,
                          final long chatId,
                          final MainMenuButtonBuilder mainMenuButtonBuilder,
                          final MainMenuButtonService mainMenuButtonService) {
        super(messageSender, chatId);
        this.mainMenuButtonBuilder = mainMenuButtonBuilder;
        this.mainMenuButtonService = mainMenuButtonService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String message = "На жаль, я не розумію цю команду. Будь ласка, використовуйте кнопки меню.";
        messageSender.sendMessage(chatId, message,
                                  mainMenuButtonBuilder.build(mainMenuButtonService.getMainMenuButtons()));
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