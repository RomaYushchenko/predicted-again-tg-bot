package com.ua.yushchenko.service.telegram;

import com.ua.yushchenko.menu.MenuFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Реалізація сервісу Telegram бота.
 * Відповідає за взаємодію з Telegram API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {
    private final TelegramMessageSender messageSender;
    private final MenuFactory menuFactory;

    @Override
    public void sendMessage(long chatId, String text) {
        try {
            messageSender.sendMessage(chatId, text);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat {}: {}", chatId, e.getMessage());
        }
    }

    @Override
    public void sendMessageWithKeyboard(long chatId, String text, ReplyKeyboard keyboard) {
        try {
            messageSender.sendMessage(chatId, text, keyboard);
        } catch (TelegramApiException e) {
            log.error("Failed to send message with keyboard to chat {}: {}", chatId, e.getMessage());
        }
    }

    @Override
    public void editMessage(long chatId, int messageId, String text) {
        try {
            messageSender.editMessage(chatId, messageId, text);
        } catch (TelegramApiException e) {
            log.error("Failed to edit message {} in chat {}: {}", messageId, chatId, e.getMessage());
        }
    }

    @Override
    public void editMessage(long chatId, int messageId, String text, InlineKeyboardMarkup keyboard) {
        try {
            messageSender.editMessage(chatId, messageId, text, keyboard);
        } catch (TelegramApiException e) {
            log.error("Failed to edit message {} with keyboard in chat {}: {}", messageId, chatId, e.getMessage());
        }
    }

    @Override
    public void showMainMenu(long chatId) {
        try {
            messageSender.showMainMenu(chatId, menuFactory.createMainMenu());
        } catch (TelegramApiException e) {
            log.error("Failed to show main menu for chat {}: {}", chatId, e.getMessage());
        }
    }

    @Override
    public void showSettings(long chatId) {
        try {
            messageSender.showSettings(chatId, menuFactory.createSettingsMenu(chatId));
        } catch (TelegramApiException e) {
            log.error("Failed to show settings for chat {}: {}", chatId, e.getMessage());
        }
    }

    @Override
    public void sendMessage(long chatId, String text, InlineKeyboardMarkup replyMarkup) {
        try {
            messageSender.sendMessage(chatId, text, replyMarkup);
        } catch (TelegramApiException e) {
            log.error("Failed to send message with inline keyboard to chat {}: {}", chatId, e.getMessage());
        }
    }
} 