package com.ua.yushchenko.command;

import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Base interface for all bot commands.
 * Defines the contract for command execution in response to user interactions.
 *
 * @author AI
 * @version 0.1-beta
 */
public interface Command {
    /**
     * Executes the command in response to a Telegram update.
     *
     * @param update the update containing message or callback query data
     * @throws TelegramApiException if there is an error executing the command
     */
    void execute(Update update) throws TelegramApiException, SchedulerException;

    /**
     * Gets the command name (e.g., "/start", "/help").
     *
     * @return the command name
     */
    String getCommandName();

    /**
     * Gets the command description for help messages.
     *
     * @return the command description
     */
    String getDescription();
} 