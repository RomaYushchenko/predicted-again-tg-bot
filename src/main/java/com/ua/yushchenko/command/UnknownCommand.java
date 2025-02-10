package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UnknownCommand extends BaseMessageCommand {
    public UnknownCommand(TelegramBot bot, long chatId,
                         PredictionService predictionService,
                         DailyPredictionService dailyPredictionService) {
        super(bot, chatId, predictionService, dailyPredictionService);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String message = "На жаль, я не розумію цю команду. Будь ласка, використовуйте кнопки меню.";
        sendMessage(message, createMainMenuKeyboard());
    }

    @Override
    public String getCommandName() {
        return "unknown";
    }

    @Override
    public String getDescription() {
        return "Невідома команда";
    }
} 