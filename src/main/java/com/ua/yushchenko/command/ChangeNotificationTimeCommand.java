package com.ua.yushchenko.command;

import com.ua.yushchenko.service.notification.NotificationService;
import com.ua.yushchenko.service.telegram.TelegramBotService;
import com.ua.yushchenko.state.BotStateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.ua.yushchenko.command.CommandConstants.*;

@Component
@RequiredArgsConstructor
public class ChangeNotificationTimeCommand implements Command {
    private final TelegramBotService telegramBotService;
    private final NotificationService notificationService;
    private final BotStateManager stateManager;

    @Override
    public void execute(Update update) throws TelegramApiException {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        
        // Встановлюємо стан очікування часу
        stateManager.setAwaitingTimeState(chatId);
        
        // Створюємо клавіатуру з кнопкою повернення до меню
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        
        InlineKeyboardButton menuButton = new InlineKeyboardButton();
        menuButton.setText(BUTTON_BACK_TO_MENU);
        menuButton.setCallbackData(CALLBACK_MENU);
        
        row.add(menuButton);
        buttons.add(row);
        keyboard.setKeyboard(buttons);

        String message = """
            ⏰ Будь ласка, введіть час для щоденних сповіщень у форматі ГГ:ХХ
            
            Наприклад: 09:00 або 21:30
            
            Щоб скасувати, натисніть кнопку "Назад у меню" нижче""";
        
        telegramBotService.sendMessage(chatId, message, keyboard);
    }
} 