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
        final List<Prediction> allPredictions = predictionRepository.findAll();
        final int sizeAllPredictions = allPredictions.size();

        final List<String> allPredictionOfUser = getAllPredictionOfUser(user, sizeAllPredictions);

        Prediction prediction;

        do {
            prediction = allPredictions.get(randomGenerator.nextInt(sizeAllPredictions));
        } while (allPredictionOfUser.contains(prediction.getText()) && allPredictionOfUser.size() > 1);

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

    private List<String> getAllPredictionOfUser(final User user, final int sizeAllPredictions) {
        final List<UserPrediction> userPredictions = userPredictionRepository.findAllByUserOrderBySentAtDesc(user);

        return userPredictions.size() >= sizeAllPredictions
                ? cleanPredictionsForUser(userPredictions)
                : userPredictions.stream()
                                 .map(UserPrediction::getPrediction)
                                 .toList();
    }

    private List<String> cleanPredictionsForUser(final List<UserPrediction> userPredictions) {
        userPredictionRepository.deleteAll(userPredictions);
        return Collections.emptyList();
    }
} 