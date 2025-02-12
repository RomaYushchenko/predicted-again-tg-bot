package com.ua.yushchenko.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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

    @Override
    public void sendMessage(long chatId, String text) {
        try {
            messageSender.sendMessage(chatId, text);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat {}: {}", chatId, e.getMessage());
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