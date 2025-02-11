package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnotherPredictionCommand extends BaseCallbackCommand {

    public AnotherPredictionCommand(TelegramBot bot, long chatId, int messageId,
                                    PredictionService predictionService,
                                    DailyPredictionService dailyPredictionService) {
        super(bot, chatId, messageId, predictionService, dailyPredictionService);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String prediction = predictionService.generateQuickPrediction(chatId);
        editMessage(prediction, createPredictionInlineKeyboard());
    }

    @Override
    protected void handleMessageNotModified() {
        try {
            execute(null); // Повторна спроба з новим передбаченням
        } catch (TelegramApiException e) {
            handleError(e);
        }
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