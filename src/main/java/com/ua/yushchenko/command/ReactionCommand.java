package com.ua.yushchenko.command;

import com.ua.yushchenko.builder.ui.reaction.ReactionButtonBuilder;
import com.ua.yushchenko.model.ReactionType;
import com.ua.yushchenko.service.reaction.ReactionService;
import com.ua.yushchenko.service.telegram.MessageSender;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling Reaction requests.
 * Shows the reaction and allows users to modify them.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
public class ReactionCommand extends AbstractCallbackCommand {

    private final ReactionType reactionType;
    private final long predictionId;
    private final ReactionService reactionService;
    private final ReactionButtonBuilder reactionButtonBuilder;

    protected ReactionCommand(final MessageSender messageSender,
                              final long chatId,
                              final int messageId,
                              final long predictionId,
                              final ReactionType reactionType,
                              final ReactionService reactionService,
                              final ReactionButtonBuilder reactionButtonBuilder) {
        super(messageSender, chatId, messageId);
        this.predictionId = predictionId;
        this.reactionType = reactionType;
        this.reactionService = reactionService;
        this.reactionButtonBuilder = reactionButtonBuilder;
    }

    @Override
    public void execute(final Update update) throws TelegramApiException, SchedulerException {
        reactionService.addReaction(chatId, predictionId, reactionType);

        final Message message = (Message) update.getCallbackQuery().getMessage();
        String messageText = message.getText();
        messageSender.editMessage(chatId, messageId, messageText,
                                  reactionButtonBuilder.buildCallback(chatId, predictionId));
    }

    @Override
    public String getCommandName() {
        return "reaction";
    }

    @Override
    public String getDescription() {
        return "reaction";
    }
}
