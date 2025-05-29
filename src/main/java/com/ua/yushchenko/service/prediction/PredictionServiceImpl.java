package com.ua.yushchenko.service.prediction;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        if (allPredictions.size() <= 1) {
            return allPredictions.get(0);
        }

        final List<Prediction> filtered = allPredictions.stream()
                                                        .filter(p -> !p.getText().equals(lastPrediction))
                                                        .toList();

        if (filtered.isEmpty()) {
            return allPredictions.get(0);
        }

        return filtered.get(randomGenerator.nextInt(filtered.size()));
    }


    @Override
    public Prediction generateUniquePrediction(final long chatId) {
        final User user = userService.findByChatId(chatId);
        final List<Prediction> allPredictions = predictionRepository.findAll();
        final int totalPredictions = allPredictions.size();

        final Set<String> userPredictions = new HashSet<>(getAllPredictionOfUser(user));

        if (userPredictions.size() >= totalPredictions) {
            userPredictionRepository.deleteAllByUser(user);
            userPredictions.clear();
        }

        final List<Prediction> filtered = allPredictions.stream()
                                                        .filter(p -> !userPredictions.contains(p.getText()))
                                                        .toList();

        if (filtered.isEmpty()) {
            return allPredictions.get(0);
        }

        return filtered.get(randomGenerator.nextInt(filtered.size()));
    }

    @Override
    public void saveUserPrediction(final User user, final String prediction) {
        final var userPrediction = new UserPrediction();
        userPrediction.setUser(user);
        userPrediction.setPrediction(prediction);
        userPrediction.setSentAt(LocalDateTime.now());

        userPredictionRepository.save(userPrediction);
    }

    private List<String> getAllPredictionOfUser(final User user) {
        return userPredictionRepository.findAllByUserOrderBySentAtDesc(user)
                                       .stream()
                                       .map(UserPrediction::getPrediction)
                                       .toList();
    }

} 