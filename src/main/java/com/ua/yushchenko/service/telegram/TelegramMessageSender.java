package com.ua.yushchenko.service.telegram;

import com.ua.yushchenko.service.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementation of MessageSender interface for Telegram.
 * Handles sending messages through the Telegram API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramMessageSender implements MessageSender {

    private final TelegramApiExecutor telegramApiExecutor;

    @Override
    public void sendMessage(long chatId, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        telegramApiExecutor.execute(sendMessage);
    }

    public void sendMessage(long chatId, String text, ReplyKeyboard replyMarkup) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(replyMarkup);
        telegramApiExecutor.execute(sendMessage);
    }
} 