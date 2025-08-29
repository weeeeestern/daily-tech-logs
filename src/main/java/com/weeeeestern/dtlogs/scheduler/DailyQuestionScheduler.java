package com.weeeeestern.dtlogs.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyQuestionScheduler {

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void announce() {
        // TODO: post message to Slack channel
    }
}
