package com.ua.yushchenko.repository;

import java.util.List;
import java.util.UUID;

import com.ua.yushchenko.model.MainMenuButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for MainMenuButton entity operations.
 * Provides methods for querying and managing MainMenuButton data in the database.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Repository
public interface MainMenuButtonRepository extends JpaRepository<MainMenuButton, UUID> {

    List<MainMenuButton> findByOrderByRowNumberAscSortOrderAsc();

}
