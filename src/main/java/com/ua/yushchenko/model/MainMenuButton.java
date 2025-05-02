package com.ua.yushchenko.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entity class representing a Telegram bot MainMenuButton to prediction.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "main_menu_buttons")
public class MainMenuButton {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "button_label")
    private String buttonLabel;

    @Column(name = "command")
    private String command;

    @Column(name = "is_released")
    private boolean isReleased;

    @Column(name = "sort_order")
    private int sortOrder;

    @Column(name = "row_number")
    private int rowNumber;

    @Column(name = "date_added")
    private LocalDateTime dateAdded;
}
