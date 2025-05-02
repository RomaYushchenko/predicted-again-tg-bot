package com.ua.yushchenko.service.mainmenubutton;

import java.util.List;

import com.ua.yushchenko.model.MainMenuButton;
import com.ua.yushchenko.repository.MainMenuButtonRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Class that represented logic to main menu button
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Service
@RequiredArgsConstructor
public class MainMenuButtonServiceImpl implements MainMenuButtonService {

    @NonNull
    private final MainMenuButtonRepository repository;

    @Override
    public List<MainMenuButton> getMainMenuButtons() {
        return repository.findByOrderByRowNumberAscSortOrderAsc();
    }

    @Override
    public void updates(final List<MainMenuButton> mainMenuButtonsToUpdate) {
        repository.saveAll(mainMenuButtonsToUpdate);
    }
}
