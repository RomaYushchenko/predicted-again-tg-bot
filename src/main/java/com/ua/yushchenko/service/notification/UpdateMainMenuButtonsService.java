package com.ua.yushchenko.service.notification;

import java.util.List;

import com.ua.yushchenko.builder.ui.main.MainMenuButtonBuilder;
import com.ua.yushchenko.model.MainMenuButton;
import com.ua.yushchenko.service.mainmenubutton.MainMenuButtonService;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Class that represented logic to update main menu
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateMainMenuButtonsService {

    @NonNull
    private final MainMenuButtonService mainMenuButtonService;
    @NonNull
    private final MainMenuButtonBuilder mainMenuButtonBuilder;
    @NonNull
    private final UserService userService;
    @NonNull
    private final MessageSender messageSender;

    @PostConstruct
    public void init() {
        final List<MainMenuButton> mainMenuButtons = mainMenuButtonService.getMainMenuButtons();

        if (mainMenuButtons.stream().anyMatch(mainMenuButton -> !mainMenuButton.isReleased())) {

            final ReplyKeyboardMarkup mainMenuKeyboard = mainMenuButtonBuilder.build(mainMenuButtons);

            final StringBuilder message = new StringBuilder();

            message.append("\uD83E\uDE84").append(" Щось новеньке витає у повітрі...").append("\n")
                    .append("Наш магічний світ передбачень поповнився новими чарами:").append("\n");

            mainMenuButtons.stream()
                           .filter(menuButton -> !menuButton.isReleased())
                           .forEach(menuButton -> {
                               message.append(" - ").append(menuButton.getButtonLabel()).append("\n");
                           });

            message.append("\n").append("Вже готові спробувати свою удачу? Зазирніть у меню й пориньте у нові передбачення! ✨");

            userService.findAll()
                       .forEach(user -> {
                           final Long chatId = user.getChatId();
                           try {
                               messageSender.sendMessage(chatId, message.toString(), mainMenuKeyboard);
                           } catch (TelegramApiException e) {
                               log.error("Unexpected Exception: {}", e.getMessage(), e);
                           }
                       });

            final var mainMenuButtonsToUpdate = mainMenuButtons.stream()
                                                               .map(UpdateMainMenuButtonsService::buildUpdatedMainMenuButton)
                                                               .toList();

            mainMenuButtonService.updates(mainMenuButtonsToUpdate);
        }
    }

    private static MainMenuButton buildUpdatedMainMenuButton(final MainMenuButton menuButton) {
        final MainMenuButton mainMenuButton = new MainMenuButton();

        mainMenuButton.setId(menuButton.getId());
        mainMenuButton.setButtonLabel(menuButton.getButtonLabel());
        mainMenuButton.setCommand(menuButton.getCommand());

        mainMenuButton.setReleased(true);

        mainMenuButton.setSortOrder(menuButton.getSortOrder());
        mainMenuButton.setRowNumber(menuButton.getRowNumber());
        mainMenuButton.setDateAdded(menuButton.getDateAdded());

        return mainMenuButton;
    }
}
