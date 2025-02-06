package com.ua.yushchenko.service.prediction;

import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implementation of PredictionService.
 * Uses database-stored predictions and provides caching.
 */
@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {
    private final UserService userService;
    private final List<Prediction> predictions;
    private final Random random = new Random();

    @Override
    public String generateQuickPrediction(long chatId) {
        List<Prediction> generalPredictions = predictions.stream()
            .filter(p -> "Загальні".equals(p.getCategory()))
            .collect(Collectors.toList());

        String prediction = generalPredictions.get(random.nextInt(generalPredictions.size())).getText();
        userService.saveLastPrediction(chatId, prediction);
        return prediction;
    }

    @Override
    public String generateDailyPrediction(long chatId) {
        String lastPrediction = userService.getLastPrediction(chatId);
        String prediction;
        do {
            prediction = predictions.get(random.nextInt(predictions.size())).getText();
        } while (prediction.equals(lastPrediction) && predictions.size() > 1);

        userService.saveLastPrediction(chatId, prediction);
        return prediction;
    }

    @Override
    public void addPredictionStrategy(PredictionStrategy strategy) {
        // Not used in current implementation
    }

    @Override
    public void removePredictionStrategy(PredictionStrategy strategy) {
        // Not used in current implementation
    }
} 