package com.ua.yushchenko.command;

/**
 * Constants class containing all command-related string constants used in the bot.
 * Includes command texts, callback data, and button labels.
 *
 * @author AI
 * @version 0.1-beta
 */
public final class CommandConstants {

    private CommandConstants() {
        // Prevent instantiation
    }

    /**
     * Command text constants used for direct bot commands.
     */
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_SETTINGS = "/settings";
    public static final String COMMAND_QUICK = "/quick";
    public static final String COMMAND_DAILY = "/daily";
    public static final String COMMAND_QUICK_PREDICTION = "🎲 Швидке передбачення";
    public static final String COMMAND_DAILY_PREDICTION = "📅 Щоденне передбачення";
    public static final String COMMAND_SETTINGS_BUTTON = "⚙️ Налаштування";

    /**
     * Callback data constants used for inline keyboard buttons.
     */
    public static final String CALLBACK_SETTINGS = "settings";
    public static final String CALLBACK_TOGGLE_NOTIFICATIONS = "toggle_notifications";
    public static final String CALLBACK_CHANGE_TIME = "change_notification_time";
    public static final String CALLBACK_ANOTHER_PREDICTION = "another_prediction";
    public static final String CALLBACK_REACTION_SUPER = "reaction_super_";
    public static final String CALLBACK_REACTION_FUNNY = "reaction_funny_";
    public static final String CALLBACK_REACTION_BAD = "reaction_bad_";

    public static final String QUICK_PREFIX = "q_";
    public static final String DAILY_PREFIX = "d_";

    /**
     * Button text constants used for keyboard buttons.
     */
    public static final String BUTTON_BACK_TO_MENU = "🔙 Назад у меню";
    public static final String BUTTON_ANOTHER_PREDICTION = "🎲 Ще одне передбачення";
    public static final String BUTTON_CHANGE_TIME = "⏰ Змінити час сповіщень";
    public static final String BUTTON_ENABLE_NOTIFICATIONS = "🔔 Увімкнути сповіщення";
    public static final String BUTTON_DISABLE_NOTIFICATIONS = "🔕 Вимкнути сповіщення";
} 