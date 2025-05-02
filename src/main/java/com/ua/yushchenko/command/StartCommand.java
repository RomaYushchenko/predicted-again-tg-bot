package com.ua.yushchenko.command;

import java.time.LocalDateTime;

import com.ua.yushchenko.builder.ui.main.MainMenuButtonBuilder;
import com.ua.yushchenko.service.mainmenubutton.MainMenuButtonService;
import com.ua.yushchenko.service.notification.NotificationSchedulerService;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.service.user.UserService;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling start menu requests.
 *
 * @author AI
 * @version 0.1-beta
 */
public class StartCommand extends AbstractMessageCommand {

    private final NotificationSchedulerService notificationSchedulerService;
    private final UserService userService;
    private final MainMenuButtonBuilder mainMenuButtonBuilder;
    private final MainMenuButtonService mainMenuButtonService;

    protected StartCommand(final MessageSender messageSender,
                           final long chatId,
                           final NotificationSchedulerService notificationSchedulerService,
                           final UserService userService,
                           final MainMenuButtonBuilder mainMenuButtonBuilder,
                           final MainMenuButtonService mainMenuButtonService) {
        super(messageSender, chatId);
        this.notificationSchedulerService = notificationSchedulerService;
        this.userService = userService;
        this.mainMenuButtonBuilder = mainMenuButtonBuilder;
        this.mainMenuButtonService = mainMenuButtonService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException, SchedulerException {
        final LocalDateTime notificationTime = LocalDateTime.now()
                                                            .withHour(9)
                                                            .withMinute(0)
                                                            .withSecond(0)
                                                            .withNano(0);

        userService.saveNotificationTime(chatId, notificationTime);
        notificationSchedulerService.scheduleDailyNotification(chatId, notificationTime);

        String welcomeMessage = """
                👋 Вітаю! Я бот передбачень, який допоможе вам дізнатися, що чекає на вас у майбутньому.
                
                🎲 Ви можете отримати швидке передбачення прямо зараз
                📅 Або налаштувати щоденні передбачення у зручний для вас час
                ⚙️ У налаштуваннях ви можете керувати сповіщеннями
                
                ℹ️ За замовчуванням щоденні сповіщення увімкнені та встановлені на 09:00
                
                Оберіть опцію з меню нижче:""";

        messageSender.sendMessage(chatId, welcomeMessage,
                                  mainMenuButtonBuilder.build(mainMenuButtonService.getMainMenuButtons()));

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