package com.ua.yushchenko.events;

import com.ua.yushchenko.model.events.MagicBallEvent;
import com.ua.yushchenko.service.magicball.MagicBallService;
import com.ua.yushchenko.service.telegram.MessageSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MagicBallKafkaConsumer {

    @NonNull
    private final MagicBallService magicBallService;
    @NonNull
    private final MessageSender messageSender;

    @KafkaListener(topics = "${app.topics.magic-ball}", groupId = "magic-ball-group")
    public void listen(ConsumerRecord<String, MagicBallEvent> record) throws TelegramApiException {
        MagicBallEvent event = record.value();

        log.info("⚡️ Received MagicBallEvent: {}", event);

        try {
            String reply = magicBallService.getMagicBallResponse(event.getQuestion());
            messageSender.sendMessage(event.getChatId(), reply);
        } catch (Exception e) {
            log.error("❌ Failed to process MagicBallEvent: {}", event, e);
            messageSender.sendMessage(event.getChatId(), "🌀 Щось пішло не так. Магічна куля зависла…");
        }
    }
}
