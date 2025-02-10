package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BackToMenuCommand extends BaseCallbackCommand {
    public BackToMenuCommand(TelegramBot bot, long chatId, int messageId,
                           PredictionService predictionService,
                           DailyPredictionService dailyPredictionService) {
        super(bot, chatId, messageId, predictionService, dailyPredictionService);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        sendMessage("Головне меню:", createMainMenuKeyboard());
    }

    @Override
    public String getCommandName() {
        return "back_to_menu";
    }

    @Override
    public String getDescription() {
        return "Повернутися до головного меню";
    }
} 