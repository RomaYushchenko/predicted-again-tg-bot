package com.ua.yushchenko.command;

import java.time.format.DateTimeFormatter;

import com.ua.yushchenko.builder.ui.settings.SettingButtonBuilder;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.service.user.UserService;
import com.ua.yushchenko.state.BotStateManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Command for handling settings menu requests.
 * Shows the current notification settings and allows users to modify them.
 *
 * @author AI
 * @version 0.1-beta
 */
public class SettingsCommand extends AbstractMessageCommand {

    /**
     * Formatter for displaying notification times
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final UserService userService;
    private final BotStateManager stateManager;
    private final SettingButtonBuilder settingButtonBuilder;

    /**
     * Creates a new settings command.
     *
     * @param messageSender the bot instance
     * @param chatId        ID of the chat where the command was invoked
     * @param userService   service for handling daily predictions
     */
    public SettingsCommand(final MessageSender messageSender,
                           final long chatId,
                           final UserService userService,
                           final BotStateManager stateManager,
                           final SettingButtonBuilder settingButtonBuilder) {
        super(messageSender, chatId);
        this.userService = userService;
        this.stateManager = stateManager;
        this.settingButtonBuilder = settingButtonBuilder;
    }

    /**
     * Executes the settings command.
     * Shows current notification settings and available options.
     *
     * @param update the update containing the command request
     * @throws TelegramApiException if there is an error sending the message
     */
    @Override
    public void execute(Update update) throws TelegramApiException {
        final boolean notificationsEnabled = userService.isNotificationsEnabled(chatId);
        final StringBuilder message = new StringBuilder("⚙️ Налаштування\n\n");

        message.append(notificationsEnabled
                               ? "🔔 Сповіщення: Увімкнено"
                               : "🔕 Сповіщення: Вимкнено")
               .append("\n");

        userService.getNotificationTime(chatId)
                   .ifPresentOrElse(time -> message.append("🕒 Час сповіщень: ").append(time.format(TIME_FORMATTER)),
                                    () -> message.append("⚠️ Час сповіщень: Не встановлено"));

        final var settingKeyboard = settingButtonBuilder.buildKeyboard(notificationsEnabled);

        if (update.hasMessage()) {
            messageSender.sendMessage(chatId, message.toString(), settingKeyboard);
        } else {
            stateManager.clearState(chatId);
            messageSender.editMessage(chatId, update.getCallbackQuery().getMessage().getMessageId(),
                                      message.toString(), settingKeyboard);
        }
    }

    @Override
    public String getCommandName() {
        return "/settings";
    }

    @Override
    public String getDescription() {
        return "Налаштування сповіщень та часу отримання передбачень";
    }
} 