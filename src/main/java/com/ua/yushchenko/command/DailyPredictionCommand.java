package com.ua.yushchenko.command;

import static com.ua.yushchenko.command.CommandConstants.DAILY_PREFIX;

import java.util.Optional;

import com.ua.yushchenko.builder.ui.prediction.DailyPredictionButtonBuilder;
import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.service.user.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DailyPredictionCommand extends AbstractMessageCommand {

    private final PredictionService predictionService;
    private final UserService userService;
    private final DailyPredictionButtonBuilder dailyPredictionButtonBuilder;

    public DailyPredictionCommand(final MessageSender messageSender,
                                  final long chatId,
                                  final PredictionService predictionService,
                                  final UserService userService,
                                  final DailyPredictionButtonBuilder dailyPredictionButtonBuilder) {
        super(messageSender, chatId);

        this.predictionService = predictionService;
        this.userService = userService;
        this.dailyPredictionButtonBuilder = dailyPredictionButtonBuilder;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        final String lastPrediction = userService.getLastPrediction(chatId);
        final Optional<Prediction> prediction = predictionService.getPredictionByText(lastPrediction);

        if (prediction.isEmpty()) {
            messageSender.sendMessage(chatId,
                                      "У тебе ще немає щоденного передбачення. Перейди в меню «⚙️ Налаштування», щоб " +
                                              "активувати сповіщення про передбачення.");
            return;
        }

        messageSender.sendMessage(chatId, prediction.get().getText(),
                                  dailyPredictionButtonBuilder.buildKeyboard(chatId, prediction.get().getId(), DAILY_PREFIX));
    }

    @Override
    public String getCommandName() {
        return "/daily";
    }

    @Override
    public String getDescription() {
        return "Отримати щоденне передбачення";
    }
} 