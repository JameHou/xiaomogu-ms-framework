package com.github.xiaomogu.framework.feign.job;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @auther JameHou
 * @date 2019/1/10 9:41
 */
@Component
@Slf4j
public class OrderAutoCancelJob {
    private static final String FOURTEEN_MIN = "PT14M";

    /**
     *  lockAtMostForString 最长锁多少时间（单位ms），确保如果节点挂了，这个锁还能保持多少时间
     *  lockAtLeastForString 最小锁多少时间（单位ms），确保多少时间内只会执行一次
     */
    @Scheduled(cron = "*/5 * * * * ?")//5秒一次
    @SchedulerLock(name = "demoTaskName", lockAtMostForString = FOURTEEN_MIN, lockAtLeastForString = FOURTEEN_MIN)
    public void scheduledTask() {
        // do something
        log.info("feign order auto cancel job start execution.............................................");
    }
}
