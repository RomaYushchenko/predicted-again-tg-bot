package com.ua.yushchenko.menu;

public class DailyPredictionMenu extends AbstractInlineMenu {
    public DailyPredictionMenu() {
        addButton("🔄 Отримати інше передбачення", "GET_ANOTHER_DAILY_PREDICTION");
        addButton("🕒 Змінити час гадання", "CHANGE_NOTIFICATION_TIME");
        addButton("🔙 Назад у меню", "BACK_TO_MENU");
    }
} 