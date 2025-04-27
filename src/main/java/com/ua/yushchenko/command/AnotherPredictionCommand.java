package com.ua.yushchenko.command;

import static com.ua.yushchenko.command.CommandConstants.QUICK_PREFIX;

import com.ua.yushchenko.builder.ui.prediction.QuickPredictionButtonBuilder;
import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.telegram.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnotherPredictionCommand extends AbstractCallbackCommand {

    private final PredictionService predictionService;
    private final QuickPredictionButtonBuilder quickPredictionButtonBuilder;

    public AnotherPredictionCommand(final MessageSender messageSender,
                                    final long chatId,
                                    final int messageId,
                                    final PredictionService predictionService,
                                    final QuickPredictionButtonBuilder quickPredictionButtonBuilder) {
        super(messageSender, chatId, messageId);

        this.predictionService = predictionService;
        this.quickPredictionButtonBuilder = quickPredictionButtonBuilder;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        final Prediction prediction = predictionService.generateQuickPrediction(chatId);

        messageSender.editMessage(chatId, messageId, prediction.getText(),
                                  quickPredictionButtonBuilder.buildKeyboard(prediction.getId(), QUICK_PREFIX));
    }

    @Override
    public String getCommandName() {
        return "another_prediction";
    }

    @Override
    public String getDescription() {
        return "Отримати ще одне передбачення";
    }
} 