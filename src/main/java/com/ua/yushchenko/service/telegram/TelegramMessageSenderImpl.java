package com.ua.yushchenko.service.telegram;

import com.ua.yushchenko.service.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementation of MessageSender interface that uses TelegramBotService.
 * Provides functionality for sending messages via Telegram.
 */
@Service
@RequiredArgsConstructor
public class TelegramMessageSenderImpl implements MessageSender {
    
    private final TelegramBotService telegramBotService;
    
    @Override
    public void sendMessage(long chatId, String message) throws TelegramApiException {
        telegramBotService.sendMessage(chatId, message);
    }
} 