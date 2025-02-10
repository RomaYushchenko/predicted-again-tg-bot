package com.ua.yushchenko.command;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.service.DailyPredictionService;
import com.ua.yushchenko.service.prediction.PredictionService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.ua.yushchenko.command.CommandConstants.*;

public class AnotherPredictionCommand extends BaseCallbackCommand {

    public AnotherPredictionCommand(TelegramBot bot, long chatId, int messageId,
                                  PredictionService predictionService,
                                  DailyPredictionService dailyPredictionService) {
        super(bot, chatId, messageId, predictionService, dailyPredictionService);
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        String prediction = predictionService.generateQuickPrediction(chatId);
        editMessage(prediction, createPredictionInlineKeyboard());
    }

    @Override
    protected void handleMessageNotModified() {
        try {
            execute(null); // Повторна спроба з новим передбаченням
        } catch (TelegramApiException e) {
            handleError(e);
        }
    }

    @Override
    protected InlineKeyboardMarkup createPredictionInlineKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        
        InlineKeyboardButton anotherButton = new InlineKeyboardButton();
        anotherButton.setText(BUTTON_ANOTHER_PREDICTION);
        anotherButton.setCallbackData(CALLBACK_ANOTHER_PREDICTION);
        
        InlineKeyboardButton menuButton = new InlineKeyboardButton();
        menuButton.setText(BUTTON_BACK_TO_MENU);
        menuButton.setCallbackData(CALLBACK_MENU);
        
        row.add(anotherButton);
        row.add(menuButton);
        buttons.add(row);
        keyboard.setKeyboard(buttons);
        
        return keyboard;
    }

    @Override
    public String getCommandName() {
        return "another_prediction";
    }

    @Override
    public String getDescription() {
        return "Отримати ще одне передбачення";
    }
} 