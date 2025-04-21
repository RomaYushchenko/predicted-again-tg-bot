package com.ua.yushchenko.command;

import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.telegram.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnotherPredictionCommand extends AbstractCallbackCommand {

    private final PredictionService predictionService;

    public AnotherPredictionCommand(MessageSender messageSender,
                                    long chatId,
                                    int messageId,
                                    PredictionService predictionService) {
        super(messageSender, chatId, messageId);

        this.predictionService = predictionService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String prediction = predictionService.generateQuickPrediction(chatId);
        messageSender.editMessage(chatId, messageId, prediction, createPredictionInlineKeyboard());
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