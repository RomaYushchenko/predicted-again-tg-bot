package com.ua.yushchenko.menu;

public class PredictionMenu extends AbstractInlineMenu {
    public PredictionMenu() {
        addButton("🔄 Отримати ще одне", "GET_ANOTHER_PREDICTION");
        addButton("🔙 Назад у меню", "BACK_TO_MENU");
    }
} 