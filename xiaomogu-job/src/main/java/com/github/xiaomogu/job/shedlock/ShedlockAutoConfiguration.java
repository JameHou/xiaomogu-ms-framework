package com.github.xiaomogu.job.shedlock;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/** 轻量级分布式定时任务
 * <link ref "https://github.com/lukas-krecan/ShedLock" />
 * @auther JameHou
 * @date 2019/1/9 20:32
 */
@Configuration
@ConditionalOnClass({LockProvider.class})
@ConditionalOnBean(RedisConnectionFactory.class)
//@AutoConfigureAfter(RedisConnectionFactory.class)
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT10M")
@Slf4j
public class ShedlockAutoConfiguration {

    @Value("${spring.profiles.active}")
    private String evn;
    @Value("${mogo.task.poolSize:10}")
    private Integer poolSize;

    @Bean
    public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
        return new RedisLockProvider(connectionFactory, evn);
    }


    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix("mogo-task-schedule-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setErrorHandler(t -> log.error(
                "Unknown error occurred while executing task.", t
        ));
        scheduler.setRejectedExecutionHandler(
                (r, e) -> log.error(
                        "Execution of task {} was rejected for unknown reasons.", r
                )
        );

        return scheduler;
    }

}
