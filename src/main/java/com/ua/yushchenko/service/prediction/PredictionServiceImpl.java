package com.ua.yushchenko.service.prediction;

import com.ua.yushchenko.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Реалізація сервісу передбачень.
 * Використовує паттерн Strategy для генерації різних типів передбачень.
 */
@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {
    private final UserService userService;
    private final Random random = new Random();

    private static final List<String> QUICK_PREDICTIONS = List.of(
        "Сьогодні буде чудовий день! 🌟",
        "На вас чекає приємний сюрприз! 🎁",
        "Ваші мрії скоро здійсняться! ✨",
        "Удача на вашому боці! 🍀",
        "Попереду важливі зміни! 🔄",
        "Ви отримаєте гарні новини! 📨",
        "Вас чекає цікава зустріч! 👥",
        "Ваші зусилля будуть винагороджені! 🏆",
        "Настав час для нових починань! 🚀",
        "Вас очікує фінансовий успіх! 💰",
        "Ваше бажання здійсниться! 🌠",
        "На вас чекає приємна подорож! ✈️"
    );

    private static final List<String> DAILY_PREDICTIONS = List.of(
        "Сьогодні ваш день буде наповнений радістю та успіхом! 🌞",
        "День принесе важливі відкриття та нові можливості! 🎯",
        "Сьогодні ви зможете вирішити складне питання! 🎊",
        "День буде продуктивним та успішним! 📈",
        "Сьогодні вас чекає приємна несподіванка! 🎁",
        "День принесе гарні новини та позитивні емоції! 😊",
        "Сьогодні ви отримаєте відповідь на важливе питання! 🔑",
        "День буде наповнений цікавими зустрічами! 👥",
        "Сьогодні ваші мрії почнуть здійснюватися! ⭐",
        "День принесе фінансовий успіх та стабільність! 💰",
        "Сьогодні ви зможете досягти поставленої мети! 🎯",
        "День буде сповнений любові та гармонії! ❤️"
    );

    @Override
    public String generateQuickPrediction(long chatId) {
        String prediction = QUICK_PREDICTIONS.get(random.nextInt(QUICK_PREDICTIONS.size()));
        userService.saveLastPrediction(chatId, prediction);
        return prediction;
    }

    @Override
    public String generateDailyPrediction(long chatId) {
        String prediction = DAILY_PREDICTIONS.get(random.nextInt(DAILY_PREDICTIONS.size()));
        userService.saveLastPrediction(chatId, prediction);
        return prediction;
    }

    @Override
    public void addPredictionStrategy(PredictionStrategy strategy) {
        // Не використовується в поточній реалізації
    }

    @Override
    public void removePredictionStrategy(PredictionStrategy strategy) {
        // Не використовується в поточній реалізації
    }
} 