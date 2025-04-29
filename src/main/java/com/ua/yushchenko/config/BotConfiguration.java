package com.ua.yushchenko.config;

import com.ua.yushchenko.bot.TelegramBot;
import com.ua.yushchenko.builder.ui.prediction.DailyPredictionButtonBuilder;
import com.ua.yushchenko.builder.ui.prediction.QuickPredictionButtonBuilder;
import com.ua.yushchenko.builder.ui.reaction.ReactionButtonBuilder;
import com.ua.yushchenko.builder.ui.settings.SettingButtonBuilder;
import com.ua.yushchenko.command.CommandFactory;
import com.ua.yushchenko.common.SplitMix64RandomGenerator;
import com.ua.yushchenko.repository.PredictionRepository;
import com.ua.yushchenko.repository.UserPredictionRepository;
import com.ua.yushchenko.repository.UserRepository;
import com.ua.yushchenko.service.notification.NotificationSchedulerService;
import com.ua.yushchenko.service.prediction.PredictionService;
import com.ua.yushchenko.service.prediction.PredictionServiceImpl;
import com.ua.yushchenko.service.reaction.ReactionService;
import com.ua.yushchenko.service.telegram.MessageSender;
import com.ua.yushchenko.service.telegram.MessageSenderImpl;
import com.ua.yushchenko.service.telegram.TelegramApiExecutor;
import com.ua.yushchenko.service.telegram.TelegramApiExecutorImpl;
import com.ua.yushchenko.service.user.UserService;
import com.ua.yushchenko.service.user.UserServiceImpl;
import com.ua.yushchenko.state.BotStateManager;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Configuration class for Telegram bot and related services.
 * Provides beans for bot initialization and message handling.
 * Configures task scheduler for asynchronous operations.
 *
 * @author AI
 * @version 0.1-beta
 */
@Slf4j
@Configuration
public class BotConfiguration {

    private static final int SCHEDULER_POOL_SIZE = 5;
    private static final String SCHEDULER_THREAD_PREFIX = "TaskScheduler-";

    private final Dotenv dotenv;

    public BotConfiguration(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    /**
     * Validates bot configuration after construction.
     * Checks if bot token and name are properly configured.
     *
     * @throws IllegalStateException if bot token or name is not configured
     */
    @PostConstruct
    public void validateConfig() {
        String botToken = dotenv.get("BOT_TOKEN");
        String botName = dotenv.get("BOT_NAME");

        if (botToken == null || botToken.isEmpty()) {
            throw new IllegalStateException("Bot token is not configured");
        }

        if (botName == null || botName.isEmpty()) {
            throw new IllegalStateException("Bot name is not configured");
        }

        log.info("Bot configuration validated successfully");
    }

    /**
     * Creates a TaskScheduler bean for handling asynchronous operations.
     *
     * @return configured ThreadPoolTaskScheduler instance
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(SCHEDULER_POOL_SIZE);
        scheduler.setThreadNamePrefix(SCHEDULER_THREAD_PREFIX);
        scheduler.setDaemon(true);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setErrorHandler(throwable ->
                                          log.error("Error in scheduled task: {}", throwable.getMessage())
                                 );
        return scheduler;
    }

    /**
     * Creates CommandFactory bean for handling bot commands.
     *
     * @param messageSender                the main bot instance
     * @param predictionService            service for generating predictions
     * @param notificationSchedulerService service for managing notifications
     * @param stateManager                 manager for bot states
     * @return configured CommandFactory instance
     */
    @Bean
    public CommandFactory commandFactory(MessageSender messageSender,
                                         PredictionService predictionService,
                                         NotificationSchedulerService notificationSchedulerService,
                                         BotStateManager stateManager,
                                         UserService userService,
                                         final ReactionService reactionService,
                                         final ReactionButtonBuilder reactionButtonBuilder,
                                         final QuickPredictionButtonBuilder quickPredictionButtonBuilder,
                                         final DailyPredictionButtonBuilder dailyPredictionButtonBuilder,
                                         final SettingButtonBuilder settingButtonBuilder) {
        return new CommandFactory(messageSender, predictionService, notificationSchedulerService, stateManager,
                                  userService, reactionService, reactionButtonBuilder, quickPredictionButtonBuilder,
                                  dailyPredictionButtonBuilder, settingButtonBuilder);
    }

    /**
     * Creates the main TelegramBot bean.
     *
     * @param botSettings    bot configuration
     * @param messageSender  service for Telegram API interactions
     * @param commandFactory factory for creating command instances
     * @return configured TelegramBot instance
     */
    @Bean
    public TelegramBot telegramBot(BotConfig.BotSettings botSettings,
                                   MessageSender messageSender,
                                   @Lazy CommandFactory commandFactory) {
        return new TelegramBot(botSettings, messageSender, commandFactory);
    }

    /**
     * Creates TelegramApiExecutor bean for executing Telegram API methods.
     *
     * @param telegramBot the main bot instance
     * @return configured TelegramApiExecutor instance
     */
    @Bean
    public TelegramApiExecutor telegramApiExecutor(@Lazy TelegramBot telegramBot) {
        return new TelegramApiExecutorImpl(telegramBot);
    }

    /**
     * Creates TelegramMessageSender bean for sending messages to users.
     *
     * @param telegramApiExecutor executor for Telegram API methods
     * @return configured TelegramMessageSender instance
     */
    @Bean
    public MessageSenderImpl telegramMessageSender(TelegramApiExecutor telegramApiExecutor) {
        return new MessageSenderImpl(telegramApiExecutor);
    }

    /**
     * Creates MessageSender bean as a wrapper for TelegramMessageSender.
     *
     * @param telegramMessageSender the underlying message sender
     * @return configured MessageSender instance
     */
    @Bean
    public MessageSender messageSender(MessageSenderImpl telegramMessageSender) {
        return telegramMessageSender;
    }

    /**
     * Creates PredictionService bean for generating predictions.
     *
     * @param userService service for user management
     * @return configured PredictionService instance
     */
    @Bean
    public PredictionService predictionService(final UserPredictionRepository userPredictionRepository,
                                               final PredictionRepository predictionRepository,
                                               final UserService userService,
                                               final SplitMix64RandomGenerator splitMix64RandomGenerator) {
        return new PredictionServiceImpl(userPredictionRepository, predictionRepository,
                                         userService, splitMix64RandomGenerator);
    }

    /**
     * Creates UserService bean for managing user data.
     *
     * @param userRepository repository for user data persistence
     * @return configured UserService instance
     */
    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    /**
     * Creates BotStateManager bean for managing bot states.
     *
     * @return configured BotStateManager instance
     */
    @Bean
    public BotStateManager botStateManager() {
        return new BotStateManager();
    }
} 