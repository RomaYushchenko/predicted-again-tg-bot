package com.ua.yushchenko.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Implementation of MessageSender interface for Telegram.
 * Handles sending messages through the Telegram API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {

    private final TelegramApiExecutor telegramApiExecutor;

    @Override
    public void sendMessage(final long chatId, final String text) throws TelegramApiException {
        final SendMessage sendMessage = SendMessage.builder()
                                                   .chatId(chatId)
                                                   .text(text)
                                                   .build();

        telegramApiExecutor.execute(sendMessage);
    }

    @Override
    public void sendMessage(final long chatId,
                            final String text,
                            final ReplyKeyboard replyMarkup) throws TelegramApiException {
        final SendMessage sendMessage = SendMessage.builder()
                                                   .chatId(chatId)
                                                   .text(text)
                                                   .replyMarkup(replyMarkup)
                                                   .build();
        telegramApiExecutor.execute(sendMessage);
    }

    @Override
    public void sendMessage(final long chatId, final String text, final ReplyKeyboardMarkup replyMarkup) throws
            TelegramApiException {
        final SendMessage sendMessage = SendMessage.builder()
                                                   .chatId(chatId)
                                                   .text(text)
                                                   .replyMarkup(replyMarkup)
                                                   .build();
        telegramApiExecutor.execute(sendMessage);
    }

    @Override
    public void sendMessage(final BotApiMethod<?> method) throws TelegramApiException {
        telegramApiExecutor.execute(method);
    }

    @Override
    public void editMessage(final long chatId, final int messageId, final String message) throws TelegramApiException {
        final EditMessageText editMessage = EditMessageText.builder()
                                                           .chatId(String.valueOf(chatId))
                                                           .messageId(messageId)
                                                           .text(message)
                                                           .build();

        telegramApiExecutor.execute(editMessage);
    }

    @Override
    public void editMessage(final long chatId, final int messageId, final String text,
                            final InlineKeyboardMarkup replyMarkup) throws
            TelegramApiException {
        final EditMessageText editMessage = EditMessageText.builder()
                                                           .chatId(String.valueOf(chatId))
                                                           .messageId(messageId)
                                                           .text(text)
                                                           .replyMarkup(replyMarkup)
                                                           .build();

        telegramApiExecutor.execute(editMessage);
    }
} 