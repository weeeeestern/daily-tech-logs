package com.weeeeestern.dtlogs.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.weeeeestern.dtlogs.slack.SlackService;

@Component
public class DailyQuestionScheduler {

    private final SlackService slackService;
    private final String channelId;

    public DailyQuestionScheduler(SlackService slackService, @Value("${slack.channel-id:}") String channelId) {
        this.slackService = slackService;
        this.channelId = channelId;
    }

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void announce() {
        String msg = "🧠 오늘 학습할 백엔드 면접 카테고리를 선택해주세요! 예시: Spring, JVM, Database, Redis, HTTP 등 👉 /카테고리 Spring처럼 입력해주세요.";
        slackService.postMessage(channelId, msg);
    }
}
