package com.ua.yushchenko.command;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.ua.yushchenko.builder.ui.main.MainMenuButtonBuilder;
import com.ua.yushchenko.builder.ui.settings.SettingButtonBuilder;
import com.ua.yushchenko.service.mainmenubutton.MainMenuButtonService;
import com.ua.yushchenko.service.notification.NotificationSchedulerService;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.service.user.UserService;
import com.ua.yushchenko.state.BotStateManager;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TimeMessageCommand extends AbstractMessageCommand {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final BotStateManager stateManager;
    private final Message message;
    private final NotificationSchedulerService notificationSchedulerService;
    private final UserService userService;
    private final SettingButtonBuilder settingButtonBuilder;
    private final MainMenuButtonBuilder mainMenuButtonBuilder;
    private final MainMenuButtonService mainMenuButtonService;

    public TimeMessageCommand(final MessageSender messageSender,
                              final Message message,
                              final UserService userService,
                              final BotStateManager stateManager,
                              final NotificationSchedulerService notificationSchedulerService,
                              final SettingButtonBuilder settingButtonBuilder,
                              final MainMenuButtonBuilder mainMenuButtonBuilder,
                              final MainMenuButtonService mainMenuButtonService) {
        super(messageSender, message.getChatId());
        this.message = message;
        this.stateManager = stateManager;
        this.notificationSchedulerService = notificationSchedulerService;
        this.userService = userService;
        this.settingButtonBuilder = settingButtonBuilder;
        this.mainMenuButtonBuilder = mainMenuButtonBuilder;
        this.mainMenuButtonService = mainMenuButtonService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        if (!stateManager.isAwaitingTime(chatId)) {
            messageSender.sendMessage(chatId, "Головне меню:", mainMenuButtonBuilder.build(mainMenuButtonService.getMainMenuButtons()));
            return;
        }

        String timeStr = message.getText().trim();
        try {
            final LocalTime time = LocalTime.parse(timeStr, TIME_FORMATTER);

            final LocalDateTime notificationTime = LocalDateTime.now()
                                                                .withHour(time.getHour())
                                                                .withMinute(time.getMinute())
                                                                .withSecond(0)
                                                                .withNano(0);

            userService.setNotificationTime(chatId, notificationTime);
            stateManager.clearState(chatId);

            notificationSchedulerService.updateScheduleDailyNotification(chatId, notificationTime);

            if (!userService.isNotificationsEnabled(chatId)) {
                userService.enableNotifications(chatId);
            }

            final String successMessage = String.format("✅ Час сповіщень успішно встановлено на %s", time);
            messageSender.sendMessage(chatId, successMessage, settingButtonBuilder.buildBackKeyboard());
        } catch (DateTimeParseException | SchedulerException e) {
            String errorMessage = """
                    ❌ Невірний формат часу. Будь ласка, введіть час у форматі ГГ:ХХ (Час у Києві)
                    
                    Наприклад: 09:00 або 21:30
                    
                    Щоб скасувати, натисніть кнопку "До налаштувань" нижче""";

            messageSender.sendMessage(chatId, errorMessage, settingButtonBuilder.buildBackKeyboard());
        }
    }

    @Override
    public String getCommandName() {
        return "time_message";
    }

    @Override
    public String getDescription() {
        return "Обробка повідомлення з часом для сповіщень";
    }
} 