package com.ua.yushchenko.events;

import com.ua.yushchenko.model.events.MagicBallEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MagicBallKafkaProducer {

    private final KafkaTemplate<String, MagicBallEvent> kafkaTemplate;

    @Value("${app.topics.magic-ball}")
    private String topic;

    /**
     * Надсилає подію MagicBallEvent у Kafka.
     *
     * @param event об'єкт з chatId та питанням
     */
    public void send(MagicBallEvent event) {
        kafkaTemplate.send(topic, event);
    }
}