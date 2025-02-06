package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Abstract base class for commands triggered by text messages.
 * Extends BaseCommand to provide additional functionality specific to message handling.
 *
 * @author AI
 * @version 0.1-beta
 */
public abstract class MessageCommand extends BaseCommand {
    /** The message that triggered this command */
    protected final Message message;

    /**
     * Creates a new message command.
     *
     * @param bot the bot instance
     * @param message the message that triggered this command
     * @param predictionService service for generating predictions
     * @param dailyPredictionService service for handling daily predictions
     */
    protected MessageCommand(TelegramBot bot, Message message,
                           PredictionService predictionService,
                           DailyPredictionService dailyPredictionService) {
        super(bot, message.getChatId(), predictionService, dailyPredictionService);
        this.message = message;
    }

    @Override
    public abstract void execute(Update update) throws TelegramApiException;
} 