package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;

public class StartCommand extends BaseMessageCommand {
    private static final LocalTime DEFAULT_NOTIFICATION_TIME = LocalTime.of(9, 0);

    public StartCommand(TelegramBot bot, long chatId,
                       PredictionService predictionService,
                       DailyPredictionService dailyPredictionService) {
        super(bot, chatId, predictionService, dailyPredictionService);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        // Встановлюємо налаштування за замовчуванням
        if (!dailyPredictionService.isNotificationsEnabled(chatId)) {
            dailyPredictionService.setNotificationTime(chatId, DEFAULT_NOTIFICATION_TIME);
            dailyPredictionService.toggleNotifications(chatId);
        }

        String welcomeMessage = """
            👋 Вітаю! Я бот передбачень, який допоможе вам дізнатися, що чекає на вас у майбутньому.

            🎲 Ви можете отримати швидке передбачення прямо зараз
            📅 Або налаштувати щоденні передбачення у зручний для вас час
            ⚙️ У налаштуваннях ви можете керувати сповіщеннями

            ℹ️ За замовчуванням щоденні сповіщення увімкнені та встановлені на 09:00

            Оберіть опцію з меню нижче:""";
        
        sendMessage(welcomeMessage, createMainMenuKeyboard());
    }
} 