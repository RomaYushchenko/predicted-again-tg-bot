package com.ua.yushchenko.service.notification;

import java.time.LocalDateTime;

import com.ua.yushchenko.jobs.NotificationJob;
import com.ua.yushchenko.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Class that represented logic to scheduler Quartz job for notification
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Slf4j
@Service
public class NotificationSchedulerService {

    @NonNull
    private final Scheduler scheduler;
    @NonNull
    private final UserService userService;

    private final Long predictionTimeZone;

    @Autowired
    public NotificationSchedulerService(final @NonNull Scheduler scheduler,
                                        final @NonNull UserService userService,
                                        @Value("${prediction.time.zone}") final Long predictionTimeZone) {
        this.scheduler = scheduler;
        this.userService = userService;

        this.predictionTimeZone = predictionTimeZone;
    }

    @PostConstruct
    public void init() {
        userService.findAll()
                   .forEach(user -> {
                       try {
                           scheduleDailyNotification(user.getChatId(), user.getNotificationTime());
                       } catch (final SchedulerException e) {
                           log.error("Unexpected Exception: {}", e.getMessage(), e);
                       }
                   });
    }

    /**
     * Create scheduler Quartz job for notification
     *
     * @param chatId ID of chat
     * @param time   planned time
     */
    public void scheduleDailyNotification(final Long chatId, final LocalDateTime time) throws SchedulerException {
        final String jobIdentity = "user_notification_" + chatId;
        final JobKey jobKey = JobKey.jobKey(jobIdentity, "notification_group");

        final LocalDateTime kievTime = time.minusHours(predictionTimeZone);

        log.info("scheduleDailyNotification.E: Start to planning Notification daily job for user [{}] with data [{}]",
                 chatId, kievTime);

        if (scheduler.checkExists(jobKey)) {
            log.info("scheduleDailyNotification.X: Notification group {} already exists", jobIdentity);
            return;
        }

        final JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
                                              .withIdentity(jobKey)
                                              .usingJobData("chatId", chatId)
                                              .build();

        final Trigger trigger = TriggerBuilder.newTrigger()
                                              .withIdentity("trigger_" + jobIdentity, "notification_group")
                                              .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(kievTime.getHour(),
                                                                                                     kievTime.getMinute()))
                                              .forJob(jobDetail)
                                              .build();

        scheduler.scheduleJob(jobDetail, trigger);

        log.info("scheduleDailyNotification.X: Job [{}] was created for user [{}] with data [{}]", jobKey, chatId,
                 kievTime);
    }

    /**
     * Update scheduler Quartz job for notification
     *
     * @param chatId  ID of chat
     * @param newTime new planned time
     */
    public void updateScheduleDailyNotification(final Long chatId, final LocalDateTime newTime) throws
            SchedulerException {
        final String jobIdentity = "user_notification_" + chatId;
        final JobKey jobKey = JobKey.jobKey(jobIdentity, "notification_group");

        log.info(
                "updateScheduleDailyNotification.E: Start to updating Notification daily job for user [{}] with data " +
                        "[{}]",
                chatId, newTime.minusHours(predictionTimeZone));

        if (!scheduler.checkExists(jobKey)) {
            log.info("updateScheduleDailyNotification.X: Notification group {} is not already exists", jobIdentity);
            scheduleDailyNotification(chatId, newTime);
            return;
        }

        scheduler.deleteJob(jobKey);
        scheduleDailyNotification(chatId, newTime);

        log.info("updateScheduleDailyNotification.X: Job [{}] was updated for user [{}] with data [{}]",
                 jobKey, chatId, newTime.minusHours(predictionTimeZone));
    }
}
