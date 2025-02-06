package com.ua.yushchenko.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

public interface TelegramExecutor {
    <T extends Serializable> T execute(BotApiMethod<T> method) throws TelegramApiException;
} 