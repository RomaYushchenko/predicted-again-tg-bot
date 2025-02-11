package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.state.BotStateManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ChangeNotificationTimeCommand extends BaseMessageCommand {

    private final BotStateManager stateManager;

    protected ChangeNotificationTimeCommand(final TelegramBot bot,
                                            final long chatId,
                                            final PredictionService predictionService,
                                            final DailyPredictionService dailyPredictionService,
                                            final BotStateManager stateManager) {
        super(bot, chatId, predictionService, dailyPredictionService);
        this.stateManager = stateManager;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        // Встановлюємо стан очікування часу
        stateManager.setAwaitingTimeState(chatId);

        String message = """
            ⏰ Будь ласка, введіть час для щоденних сповіщень у форматі ГГ:ХХ (Час у Києві)
            
            Наприклад: 09:00 або 21:30
            
            Щоб скасувати, натисніть кнопку "Назад у меню" нижче""";

        sendMessage(message, createBackToMenuInlineKeyboard());
    }

    @Override
    public String getCommandName() {
        return "change_time";
    }

    @Override
    public String getDescription() {
        return "Змінити час отримання щоденних передбачень";
    }
} 