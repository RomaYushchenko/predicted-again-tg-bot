package com.ua.yushchenko.state;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class BotStateManager {

    private final Map<Long, BotState> userStates;

    public BotStateManager() {
        this.userStates = new ConcurrentHashMap<>();
    }

    public void setAwaitingTimeState(long chatId) {
        userStates.put(chatId, BotState.AWAITING_TIME);
    }

    public void setAwaitingQuestionState(long chatId) {
        userStates.put(chatId, BotState.AWAITING_QUESTION);
    }

    public void clearState(long chatId) {
        userStates.remove(chatId);
    }

    public BotState getState(long chatId) {
        return userStates.getOrDefault(chatId, BotState.DEFAULT);
    }

    public boolean isAwaitingTime(long chatId) {
        return getState(chatId) == BotState.AWAITING_TIME;
    }

    public boolean isAwaitingQuestion(long chatId) {
        return getState(chatId) == BotState.AWAITING_QUESTION;
    }
} 