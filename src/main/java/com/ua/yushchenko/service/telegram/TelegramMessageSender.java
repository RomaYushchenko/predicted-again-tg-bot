package com.ua.yushchenko.service.telegram;

import com.ua.yushchenko.service.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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

    public void editMessage(long chatId, int messageId, String text) throws TelegramApiException {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(text);
        telegramApiExecutor.execute(editMessage);
    }

    public void editMessage(long chatId, int messageId, String text, InlineKeyboardMarkup replyMarkup) throws TelegramApiException {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(text);
        editMessage.setReplyMarkup(replyMarkup);
        telegramApiExecutor.execute(editMessage);
    }

    public void showMainMenu(long chatId, ReplyKeyboard replyMarkup) throws TelegramApiException {
        sendMessage(chatId, "Головне меню:", replyMarkup);
    }

    public void showSettings(long chatId, ReplyKeyboard replyMarkup) throws TelegramApiException {
        sendMessage(chatId, "Налаштування:", replyMarkup);
    }
} 