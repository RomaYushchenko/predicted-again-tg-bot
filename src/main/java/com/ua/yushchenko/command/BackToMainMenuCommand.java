package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.state.BotStateManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BackToMainMenuCommand extends BaseCallbackCommand {
    private final BotStateManager stateManager;

    public BackToMainMenuCommand(TelegramBot bot, long chatId, int messageId,
                                 PredictionService predictionService,
                                 DailyPredictionService dailyPredictionService, final BotStateManager stateManager) {
        super(bot, chatId, messageId, predictionService, dailyPredictionService);
        this.stateManager = stateManager;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        stateManager.clearState(chatId);
        sendMessage("Головне меню:", createMainMenuKeyboard());
    }

    @Override
    public String getCommandName() {
        return "back_to_main_menu";
    }

    @Override
    public String getDescription() {
        return "Повернутися до головного меню";
    }
} 