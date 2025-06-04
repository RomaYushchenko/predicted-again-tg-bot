package com.ua.yushchenko.controller;

import com.ua.yushchenko.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final TelegramBot telegramBot;

    @PostMapping("/callback${bot.webhook-path}")
    public BotApiMethod<?> onUpdate(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
