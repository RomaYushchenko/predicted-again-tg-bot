package com.ua.yushchenko.command;

import com.ua.yushchenko.builder.ui.magicball.MagicBallButtonBuilder;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.state.BotStateManager;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling magic ball menu requests.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
public class MagicBallCommand extends AbstractMessageCommand {

    private final BotStateManager stateManager;
    private final MagicBallButtonBuilder magicBallButtonBuilder;

    protected MagicBallCommand(final MessageSender messageSender,
                               final long chatId,
                               final BotStateManager stateManager,
                               final MagicBallButtonBuilder magicBallButtonBuilder) {
        super(messageSender, chatId);

        this.stateManager = stateManager;
        this.magicBallButtonBuilder = magicBallButtonBuilder;
    }

    @Override
    public void execute(final Update update) throws TelegramApiException, SchedulerException {
        stateManager.setAwaitingQuestionState(chatId);

        final String message = """
                Задай питання, на яке хочеш знати відповідь!
                (Формат: питання з відповіддю «так/ні»)""";

        messageSender.sendMessage(chatId, message, magicBallButtonBuilder.buildBackKeyboard());
    }

    @Override
    public String getCommandName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }
}
