package com.ua.yushchenko.command;

import java.time.LocalDateTime;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.notification.NotificationSchedulerService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.user.UserService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling start menu requests.
 *
 * @author AI
 * @version 0.1-beta
 */
public class StartCommand extends BaseMessageCommand {

    private final Long predictionTimeZone;

    private final NotificationSchedulerService notificationSchedulerService;
    private final UserService userService;

    protected StartCommand(final TelegramBot bot,
                           final long chatId,
                           final PredictionService predictionService,
                           final DailyPredictionService dailyPredictionService,
                           final NotificationSchedulerService notificationSchedulerService,
                           final UserService userService,
                           final Long predictionTimeZone) {
        super(bot, chatId, predictionService, dailyPredictionService);
        this.notificationSchedulerService = notificationSchedulerService;
        this.userService = userService;
        this.predictionTimeZone = predictionTimeZone;
    }

    @Override
    public void execute(Update update) throws TelegramApiException, SchedulerException {
        LocalDateTime notificationTime = LocalDateTime.now()
                                                      .withHour(9)
                                                      .withMinute(0)
                                                      .withSecond(0)
                                                      .withNano(0);

        userService.saveNotificationTime(chatId, notificationTime.minusHours(predictionTimeZone));
        notificationSchedulerService.scheduleDailyNotification(chatId, notificationTime.minusHours(predictionTimeZone));

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