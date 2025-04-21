package com.ua.yushchenko.command;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.notification.NotificationSchedulerService;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.state.BotStateManager;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TimeMessageCommand extends AbstractMessageCommand {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final BotStateManager stateManager;
    private final Message message;
    private final NotificationSchedulerService notificationSchedulerService;
    private final DailyPredictionService dailyPredictionService;

    public TimeMessageCommand(final MessageSender messageSender,
                              final Message message,
                              final DailyPredictionService dailyPredictionService,
                              final BotStateManager stateManager,
                              final NotificationSchedulerService notificationSchedulerService) {
        super(messageSender, message.getChatId());
        this.message = message;
        this.stateManager = stateManager;
        this.notificationSchedulerService = notificationSchedulerService;
        this.dailyPredictionService = dailyPredictionService;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        if (!stateManager.isAwaitingTime(chatId)) {
            showMainMenu();
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

            dailyPredictionService.setNotificationTime(chatId, notificationTime);
            stateManager.clearState(chatId);

            notificationSchedulerService.updateScheduleDailyNotification(chatId, notificationTime);

            if (!dailyPredictionService.isNotificationsEnabled(chatId)) {
                dailyPredictionService.enableNotifications(chatId);
            }

            // Створюємо клавіатуру для повернення до налаштувань
            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();

            InlineKeyboardButton settingsButton = new InlineKeyboardButton();
            settingsButton.setText("⚙️ До налаштувань");
            settingsButton.setCallbackData("settings");

            row.add(settingsButton);
            buttons.add(row);
            keyboard.setKeyboard(buttons);

            String successMessage = String.format("✅ Час сповіщень успішно встановлено на %s", time);
            messageSender.sendMessage(chatId, successMessage, keyboard);
        } catch (DateTimeParseException | SchedulerException e) {
            String errorMessage = """
                    ❌ Невірний формат часу. Будь ласка, введіть час у форматі ГГ:ХХ (Час у Києві)
                    
                    Наприклад: 09:00 або 21:30
                    
                    Щоб скасувати, натисніть кнопку "До налаштувань" нижче""";

            // Створюємо клавіатуру для повернення до меню
            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();

            InlineKeyboardButton menuButton = new InlineKeyboardButton();
            menuButton.setText("⚙️ До налаштувань");
            menuButton.setCallbackData("settings");

            row.add(menuButton);
            buttons.add(row);
            keyboard.setKeyboard(buttons);

            messageSender.sendMessage(chatId, errorMessage, keyboard);
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