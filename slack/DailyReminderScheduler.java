package slack;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Schedules a daily reminder message to a Slack channel.
 */
public class DailyReminderScheduler {
    private final SlackService slackService;
    private final String channelId;
    private final ScheduledExecutorService executor;

    public DailyReminderScheduler(SlackService slackService, String channelId) {
        this.slackService = slackService;
        this.channelId = channelId;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Start scheduling the reminder at 09:00 Asia/Seoul time daily.
     */
    public void start() {
        long initialDelay = computeInitialDelay();
        long period = Duration.ofDays(1).toMillis();

        executor.scheduleAtFixedRate(() -> {
            try {
                String text = "🧠 오늘 학습할 백엔드 면접 카테고리를 골라보세요!";
                slackService.sendMessage(channelId, text);
            } catch (Exception e) {
                System.err.println("Failed to send reminder: " + e.getMessage());
                e.printStackTrace();
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    private long computeInitialDelay() {
        ZoneId seoul = ZoneId.of("Asia/Seoul");
        ZonedDateTime now = ZonedDateTime.now(seoul);
        ZonedDateTime nextRun = now.withHour(9).withMinute(0).withSecond(0).withNano(0);
        if (!now.isBefore(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }
        return Duration.between(now, nextRun).toMillis();
    }

    public static void main(String[] args) {
        String channelId = System.getenv("CHANNEL_ID");
        String botToken = System.getenv("SLACK_BOT_TOKEN");
        String signingSecret = System.getenv().getOrDefault("SLACK_SIGNING_SECRET", "");

        if (channelId == null || botToken == null) {
            System.err.println("Missing CHANNEL_ID or SLACK_BOT_TOKEN environment variables");
            return;
        }

        SlackService service = new SlackService(signingSecret, botToken);
        new DailyReminderScheduler(service, channelId).start();
    }
}

