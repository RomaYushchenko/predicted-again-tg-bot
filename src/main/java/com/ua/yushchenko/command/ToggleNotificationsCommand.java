package com.ua.yushchenko.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.ua.yushchenko.builder.ui.settings.SettingButtonBuilder;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.service.user.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ToggleNotificationsCommand extends AbstractMessageCommand {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final UserService userService;
    private final SettingButtonBuilder settingButtonBuilder;

    public ToggleNotificationsCommand(final MessageSender messageSender,
                                      final long chatId,
                                      final UserService userService,
                                      final SettingButtonBuilder settingButtonBuilder) {
        super(messageSender, chatId);

        this.userService = userService;
        this.settingButtonBuilder = settingButtonBuilder;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        userService.toggleNotifications(chatId);
        boolean wasEnabled = userService.isNotificationsEnabled(chatId);
        Optional<LocalDateTime> notificationTime = userService.getNotificationTime(chatId);

        final StringBuilder message = new StringBuilder("⚙️ Налаштування\n\n");
        message.append(wasEnabled
                               ? "🔔 Сповіщення: Увімкнено"
                               : "🔕 Сповіщення: Вимкнено")
               .append("\n")
               .append(notificationTime.map(time -> "🕒 Час сповіщень: " + time.format(TIME_FORMATTER))
                                       .orElse("⚠️ Час сповіщень: Не встановлено"));

        messageSender.editMessage(chatId, update.getCallbackQuery().getMessage().getMessageId(),
                                  message.toString(), settingButtonBuilder.buildKeyboard(wasEnabled));
    }

    @Override
    public String getCommandName() {
        return "toggle_notifications";
    }

    @Override
    public String getDescription() {
        return "Увімкнути/вимкнути сповіщення";
    }
} 