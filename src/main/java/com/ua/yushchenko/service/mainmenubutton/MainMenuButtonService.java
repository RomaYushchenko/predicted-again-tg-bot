package com.ua.yushchenko.service.mainmenubutton;

import java.util.List;

import com.ua.yushchenko.model.MainMenuButton;

/**
 * Service interface for managing main menu button.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
public interface MainMenuButtonService {

    List<MainMenuButton> getMainMenuButtons();

    void updates(final List<MainMenuButton> mainMenuButtonsToUpdate);
}
