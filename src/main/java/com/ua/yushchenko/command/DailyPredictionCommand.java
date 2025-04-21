package com.ua.yushchenko.command;

import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.telegram.MessageSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DailyPredictionCommand extends AbstractMessageCommand {

    private final PredictionService predictionService;

    public DailyPredictionCommand(final MessageSender messageSender,
                                  final long chatId,
                                  final PredictionService predictionService) {
        super(messageSender, chatId);

        this.predictionService = predictionService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String prediction = predictionService.generateDailyPrediction(chatId);
        messageSender.sendMessage(chatId, prediction, createDailyPredictionInlineKeyboard());
    }

    @Override
    public String getCommandName() {
        return "/daily";
    }

    @Override
    public String getDescription() {
        return "Отримати щоденне передбачення";
    }
} 