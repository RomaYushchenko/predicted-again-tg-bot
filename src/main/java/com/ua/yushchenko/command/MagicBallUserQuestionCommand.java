package com.ua.yushchenko.command;

import com.ua.yushchenko.events.MagicBallKafkaProducer;
import com.ua.yushchenko.model.events.MagicBallEvent;
import com.ua.yushchenko.service.client.ChatGptServiceClient;
import com.ua.yushchenko.service.telegram.MessageSender;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling magic ball question requests.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
public class MagicBallUserQuestionCommand extends AbstractMessageCommand{

    @NonNull
    private final MagicBallKafkaProducer magicBallKafkaProducer;

    protected MagicBallUserQuestionCommand(final MessageSender messageSender,
                                           final long chatId,
                                           final MagicBallKafkaProducer magicBallKafkaProducer) {
        super(messageSender, chatId);

        this.magicBallKafkaProducer = magicBallKafkaProducer;
    }

    @SneakyThrows
    @Override
    public void execute(final Update update) throws TelegramApiException, SchedulerException {
        final String userQuestion = update.getMessage().getText();

        final MagicBallEvent event = new MagicBallEvent(chatId, userQuestion);
        magicBallKafkaProducer.send(event);

        final String message = """
                Гадалка крутить шар....""";

        messageSender.sendMessage(chatId, message);
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
