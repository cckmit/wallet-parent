package org.wallet.service.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author zengfucheng
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(MailProperties.class)
@EnableScheduling
public class ScheduledConfig implements SchedulingConfigurer {
    private final ThreadPoolTaskScheduler taskScheduler;

    ScheduledConfig() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors() + 1);
        taskScheduler.setErrorHandler(t -> log.error("Exception in @Scheduled task: {}", t.getMessage(), t));
        taskScheduler.setThreadNamePrefix("@scheduled-");
        taskScheduler.initialize();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler);
    }
}
