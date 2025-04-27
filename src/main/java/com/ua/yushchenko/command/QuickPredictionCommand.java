package com.ua.yushchenko.command;

import static com.ua.yushchenko.command.CommandConstants.QUICK_PREFIX;

import com.ua.yushchenko.builder.ui.prediction.QuickPredictionButtonBuilder;
import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.telegram.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling quick prediction requests.
 * Generates and sends a random quick prediction to the user.
 *
 * @author AI
 * @version 0.1-beta
 */
public class QuickPredictionCommand extends AbstractMessageCommand {

    private final PredictionService predictionService;
    private final QuickPredictionButtonBuilder quickPredictionButtonBuilder;

    /**
     * Creates a new quick prediction command.
     *
     * @param messageSender     the bot instance
     * @param chatId            ID of the chat where the command was invoked
     * @param predictionService service for generating predictions
     */
    public QuickPredictionCommand(final MessageSender messageSender,
                                  final long chatId,
                                  final PredictionService predictionService,
                                  final QuickPredictionButtonBuilder quickPredictionButtonBuilder) {
        super(messageSender, chatId);

        this.predictionService = predictionService;
        this.quickPredictionButtonBuilder = quickPredictionButtonBuilder;
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
        final Prediction prediction = predictionService.generateQuickPrediction(chatId);

        messageSender.sendMessage(chatId, prediction.getText(),
                                  quickPredictionButtonBuilder.buildKeyboard(chatId, prediction.getId(), QUICK_PREFIX));
    }

    @Override
    public String getCommandName() {
        return "/predict";
    }

    @Override
    public String getDescription() {
        return "Отримати швидке передбачення";
    }
} 