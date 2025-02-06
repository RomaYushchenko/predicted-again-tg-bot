package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnotherDailyPredictionCommand extends BaseCallbackCommand {
    public AnotherDailyPredictionCommand(TelegramBot bot, long chatId, int messageId,
                                       PredictionService predictionService,
                                       DailyPredictionService dailyPredictionService) {
        super(bot, chatId, messageId, predictionService, dailyPredictionService);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String prediction = predictionService.generateDailyPrediction(chatId);
        editMessage(prediction, createDailyPredictionInlineKeyboard());
    }

    @Override
    protected void handleMessageNotModified() {
        try {
            execute(null); // Повторна спроба з новим передбаченням
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }
} 