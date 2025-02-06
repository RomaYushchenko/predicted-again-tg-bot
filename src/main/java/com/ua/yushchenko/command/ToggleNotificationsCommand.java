package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ToggleNotificationsCommand extends BaseMessageCommand {

    public ToggleNotificationsCommand(TelegramBot bot, long chatId,
                                   PredictionService predictionService,
                                   DailyPredictionService dailyPredictionService) {
        super(bot, chatId, predictionService, dailyPredictionService);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        boolean wasEnabled = dailyPredictionService.isNotificationsEnabled(chatId);
        dailyPredictionService.toggleNotifications(chatId);
        
        String message = wasEnabled ? "🔕 Сповіщення вимкнено" : "🔔 Сповіщення увімкнено";
        sendMessage(message, createSettingsInlineKeyboard(!wasEnabled));
    }
} 