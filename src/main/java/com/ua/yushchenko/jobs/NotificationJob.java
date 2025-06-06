package com.ua.yushchenko.jobs;

import static com.ua.yushchenko.command.CommandConstants.DAILY_PREFIX;

import java.time.LocalDateTime;
import java.util.Objects;

import com.ua.yushchenko.builder.ui.reaction.ReactionButtonBuilder;
import com.ua.yushchenko.model.Prediction;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.service.user.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Quartz job for notification
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Slf4j
public class NotificationJob implements Job {

    @NonNull
    private final UserService userService;
    @NonNull
    private final PredictionService predictionService;
    @NonNull
    private final MessageSender messageSender;
    @NonNull
    private final ReactionButtonBuilder reactionButtonBuilder;

    private final Long predictionTimeZone;

    public NotificationJob(final @NonNull UserService userService,
                           final @NonNull PredictionService predictionService,
                           final @NonNull MessageSender messageSender,
                           final @NonNull ReactionButtonBuilder reactionButtonBuilder,
                           @Value("${prediction.time.zone}") final Long predictionTimeZone) {
        this.userService = userService;
        this.predictionService = predictionService;
        this.messageSender = messageSender;
        this.reactionButtonBuilder = reactionButtonBuilder;
        this.predictionTimeZone = predictionTimeZone;
    }

    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        final LocalDateTime now = LocalDateTime.now();
        final JobDataMap dataMap = context.getMergedJobDataMap();
        final Long chatId = dataMap.getLong("chatId");

        log.info("execute.E: Starting Job for user: {}", chatId);

        final var user = userService.findByChatId(chatId);

        if (Objects.nonNull(user) && user.isNotificationsEnabled()) {

            try {
                final Prediction prediction = predictionService.generateUniquePrediction(user.getChatId());

                messageSender.sendMessage(user.getChatId(),
                                          prediction.getText(),
                                          reactionButtonBuilder.buildKeyboard(chatId, prediction.getId(), DAILY_PREFIX));

                user.setLastPrediction(prediction.getText());
                user.setLastNotificationTime(now.plusHours(predictionTimeZone));

                userService.save(user);
                predictionService.saveUserPrediction(user, prediction.getText());

                log.info("execute.X: Sending prediction [{}] to user [{}]", prediction.getText(), chatId);
                return;
            } catch (final TelegramApiException e) {
                if (e.getMessage().contains("bot was blocked by the user")) {
                    userService.removeUser(user.getId());
                    log.info("execute.X: The user [{}] who blocked the bot was deleted", chatId);
                    return;
                }

                log.error("Failed to send daily prediction to user {}: {}", chatId, e.getMessage());
            } catch (final Exception e) {
                log.error("Unexpected exception: Failed to send daily prediction to user {}: {}", chatId,
                          e.getMessage());
            }
        }

        log.info("execute.X: User [{}] doesn't exist or disable notification", chatId);
    }
}
