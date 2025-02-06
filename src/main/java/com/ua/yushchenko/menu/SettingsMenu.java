package com.ua.yushchenko.menu;

import com.ua.yushchenko.service.DailyPredictionService;

public class SettingsMenu extends AbstractInlineMenu {
    public SettingsMenu(DailyPredictionService dailyPredictionService, long chatId) {
        addButton("🕒 Змінити час гадання", "CHANGE_NOTIFICATION_TIME");
        
        boolean notificationsEnabled = dailyPredictionService.isNotificationsEnabled(chatId);
        String notificationText = notificationsEnabled ? "🔕 Вимкнути сповіщення" : "🔔 Увімкнути сповіщення";
        addButton(notificationText, "TOGGLE_NOTIFICATIONS");
        
        addButton("🏠 Назад у головне меню", "BACK_TO_MAIN_MENU");
    }
} 