package com.ua.yushchenko.model.events;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MagicBallEvent implements Serializable {

    private Long chatId;
    private String question;
}
