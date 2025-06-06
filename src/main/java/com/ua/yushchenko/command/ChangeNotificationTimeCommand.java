package com.ua.yushchenko.command;

import com.ua.yushchenko.builder.ui.settings.SettingButtonBuilder;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.state.BotStateManager;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ChangeNotificationTimeCommand extends AbstractMessageCommand {

    private final BotStateManager stateManager;
    private final SettingButtonBuilder settingButtonBuilder;

    protected ChangeNotificationTimeCommand(final MessageSender messageSender,
                                            final long chatId,
                                            final BotStateManager stateManager,
                                            final SettingButtonBuilder settingButtonBuilder) {
        super(messageSender, chatId);
        this.stateManager = stateManager;
        this.settingButtonBuilder = settingButtonBuilder;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {
        // Встановлюємо стан очікування часу
        stateManager.setAwaitingTimeState(chatId);

        String message = """
                ⏰ Будь ласка, введіть час для щоденних сповіщень у форматі ГГ:ХХ (Час у Києві)
                
                Наприклад: 09:00 або 21:30
                
                Щоб скасувати, натисніть кнопку "До налаштувань" нижче""";

        messageSender.editMessage(chatId, update.getCallbackQuery().getMessage().getMessageId(),
                                  message, settingButtonBuilder.buildBackKeyboard());
    }

    @Override
    public String getCommandName() {
        return "change_time";
    }

    @Override
    public String getDescription() {
        return "Змінити час отримання щоденних передбачень";
    }
} 