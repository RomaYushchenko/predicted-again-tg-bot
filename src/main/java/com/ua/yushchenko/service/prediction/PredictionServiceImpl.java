package com.ua.yushchenko.service.prediction;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ua.yushchenko.common.SplitMix64RandomGenerator;
import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.model.User;
import com.ua.yushchenko.model.UserPrediction;
import com.ua.yushchenko.repository.PredictionRepository;
import com.ua.yushchenko.repository.UserPredictionRepository;
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

    private final UserPredictionRepository userPredictionRepository;
    private final PredictionRepository predictionRepository;
    private final UserService userService;
    private final SplitMix64RandomGenerator randomGenerator;

    @Override
    public Optional<Prediction> getPredictionByText(final String predictionText) {
        return predictionRepository.findPredictionByText(predictionText);
    }

    @Override
    public Prediction generateQuickPrediction(long chatId) {
        final String lastPrediction = userService.getLastPrediction(chatId);
        final List<Prediction> allPredictions = predictionRepository.findAll();

        Prediction prediction;
        do {
            prediction = allPredictions.get(randomGenerator.nextInt(allPredictions.size()));
        } while (prediction.getText().equals(lastPrediction) && allPredictions.size() > 1);

        return prediction;
    }

    @Override
    public Prediction generateUniquePrediction(final long chatId) {
        final User user = userService.findByChatId(chatId);
        final List<String> top30PredictionOfUser = getTop30PredictionOfUser(user);
        final List<Prediction> allPredictions = predictionRepository.findAll();

        Prediction prediction;

        do {
            prediction = allPredictions.get(randomGenerator.nextInt(allPredictions.size()));
        } while (top30PredictionOfUser.contains(prediction.getText()) && top30PredictionOfUser.size() > 1);

        userService.saveLastPrediction(chatId, prediction.getText());
        return prediction;
    }

    @Override
    public void saveUserPrediction(final User user, final String prediction) {
        final UserPrediction userPrediction = new UserPrediction();
        userPrediction.setUser(user);
        userPrediction.setPrediction(prediction);
        userPrediction.setSentAt(LocalDateTime.now());

        userPredictionRepository.save(userPrediction);
    }

    private List<String> getTop30PredictionOfUser(final User user) {
        final List<UserPrediction> predictions = userPredictionRepository.findTop30ByUserOrderBySentAtDesc(user);

        return predictions.size() == 30
                ? cleanPredictionsForUser(predictions)
                : predictions.stream()
                             .map(UserPrediction::getPrediction)
                             .toList();
    }

    private List<String> cleanPredictionsForUser(final List<UserPrediction> predictions) {
        userPredictionRepository.deleteAll(predictions);
        return Collections.emptyList();
    }
} 