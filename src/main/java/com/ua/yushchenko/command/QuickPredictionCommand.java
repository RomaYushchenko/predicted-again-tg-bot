package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling quick prediction requests.
 * Generates and sends a random quick prediction to the user.
 *
 * @author AI
 * @version 0.1-beta
 */
public class QuickPredictionCommand extends BaseMessageCommand {
    /**
     * Creates a new quick prediction command.
     *
     * @param bot the bot instance
     * @param chatId ID of the chat where the command was invoked
     * @param predictionService service for generating predictions
     * @param dailyPredictionService service for handling daily predictions
     */
    public QuickPredictionCommand(TelegramBot bot, long chatId,
                                PredictionService predictionService,
                                DailyPredictionService dailyPredictionService) {
        super(bot, chatId, predictionService, dailyPredictionService);
    }

    /**
     * Executes the quick prediction command.
     * Generates a random prediction and sends it with an inline keyboard.
     *
     * @param update the update containing the command request
     * @throws TelegramApiException if there is an error sending the message
     */
    @Override
    public void execute(Update update) throws TelegramApiException {
        String prediction = predictionService.generateQuickPrediction(chatId);
        sendMessage(prediction, createPredictionInlineKeyboard());
    }
} 