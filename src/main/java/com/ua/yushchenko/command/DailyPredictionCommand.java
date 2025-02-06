package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DailyPredictionCommand extends BaseMessageCommand {
    public DailyPredictionCommand(TelegramBot bot, long chatId,
                                PredictionService predictionService,
                                DailyPredictionService dailyPredictionService) {
        super(bot, chatId, predictionService, dailyPredictionService);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String prediction = predictionService.generateDailyPrediction(chatId);
        sendMessage(prediction, createDailyPredictionInlineKeyboard());
    }
} 