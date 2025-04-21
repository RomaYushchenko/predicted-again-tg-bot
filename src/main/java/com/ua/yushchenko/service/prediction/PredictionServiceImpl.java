package com.ua.yushchenko.service.prediction;

import java.util.List;

import com.ua.yushchenko.common.SplitMix64RandomGenerator;
import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of PredictionService.
 * Uses database-stored predictions and provides caching.
 */
@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {

    private final UserService userService;
    private final List<Prediction> predictions;
    private final SplitMix64RandomGenerator randomGenerator;

    @Override
    public String generateQuickPrediction(long chatId) {
        final String lastPrediction = userService.getLastPrediction(chatId);

        String prediction;
        do {
            prediction = predictions.get(randomGenerator.nextInt(predictions.size())).getText();
        } while (prediction.equals(lastPrediction) && predictions.size() > 1);

        userService.saveLastPrediction(chatId, prediction);
        return prediction;
    }

    @Override
    public String generateDailyPrediction(long chatId) {
        final String lastPrediction = userService.getLastPrediction(chatId);

        String prediction;
        do {
            prediction = predictions.get(randomGenerator.nextInt(predictions.size())).getText();
        } while (prediction.equals(lastPrediction) && predictions.size() > 1);

        userService.saveLastPrediction(chatId, prediction);
        return prediction;
    }
} 