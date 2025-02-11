package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.state.BotStateManager;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TimeMessageCommand extends BaseMessageCommand {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private final BotStateManager stateManager;
    private final Message message;

    public TimeMessageCommand(TelegramBot bot, Message message,
                            PredictionService predictionService,
                            DailyPredictionService dailyPredictionService,
                            BotStateManager stateManager) {
        super(bot, message.getChatId(), predictionService, dailyPredictionService);
        this.message = message;
        this.stateManager = stateManager;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        if (!stateManager.isAwaitingTime(chatId)) {
            showMainMenu();
            return;
        }

        String timeStr = message.getText().trim();
        try {
            LocalTime time = LocalTime.parse(timeStr, TIME_FORMATTER).minusHours(2);
            LocalTime userLocalTime = LocalTime.parse(timeStr, TIME_FORMATTER);

            LocalDateTime notificationTime = LocalDateTime.now()
                .withHour(time.getHour())
                .withMinute(time.getMinute())
                .withSecond(0)
                .withNano(0);
            
            dailyPredictionService.setNotificationTime(chatId, notificationTime);
            stateManager.clearState(chatId);
            
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
            
            String successMessage = String.format("✅ Час сповіщень успішно встановлено на %s", userLocalTime.format(TIME_FORMATTER));
            sendMessage(successMessage, keyboard);
        } catch (DateTimeParseException e) {
            String errorMessage = """
                    ❌ Невірний формат часу. Будь ласка, введіть час у форматі ГГ:ХХ (Час у Києві)
                    
                    Наприклад: 09:00 або 21:30""";
            
            // Створюємо клавіатуру для повернення до меню
            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();
            
            InlineKeyboardButton menuButton = new InlineKeyboardButton();
            menuButton.setText("📋 Меню");
            menuButton.setCallbackData("back_to_menu");
            
            row.add(menuButton);
            buttons.add(row);
            keyboard.setKeyboard(buttons);
            
            sendMessage(errorMessage, keyboard);
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