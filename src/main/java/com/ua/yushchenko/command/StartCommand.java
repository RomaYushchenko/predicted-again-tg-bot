package com.ua.yushchenko.command;

import java.time.LocalDateTime;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.notification.NotificationService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.user.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling start menu requests.
 *
 * @author AI
 * @version 0.1-beta
 */
public class StartCommand extends BaseMessageCommand {

    private final NotificationService notificationService;
    private final UserService userService;

    protected StartCommand(final TelegramBot bot,
                           final long chatId,
                           final PredictionService predictionService,
                           final DailyPredictionService dailyPredictionService,
                           final NotificationService notificationService, final UserService userService) {
        super(bot, chatId, predictionService, dailyPredictionService);
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        LocalDateTime notificationTime = LocalDateTime.now()
                                                      .withHour(9)
                                                      .withMinute(0)
                                                      .withSecond(0)
                                                      .withNano(0);

        userService.saveNotificationTime(chatId, notificationTime);

        String welcomeMessage = """
                👋 Вітаю! Я бот передбачень, який допоможе вам дізнатися, що чекає на вас у майбутньому.
                
                🎲 Ви можете отримати швидке передбачення прямо зараз
                📅 Або налаштувати щоденні передбачення у зручний для вас час
                ⚙️ У налаштуваннях ви можете керувати сповіщеннями
                
                ℹ️ За замовчуванням щоденні сповіщення увімкнені та встановлені на 09:00
                
                Оберіть опцію з меню нижче:""";

        sendMessage(welcomeMessage, createMainMenuKeyboard());

    }

    @Override
    public String getCommandName() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "Почати отримувати щоденні передбачення. " +
                "Ви можете вказати час для щоденних сповіщень у форматі /start HH:mm";
    }
} 