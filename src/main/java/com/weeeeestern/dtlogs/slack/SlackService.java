package com.weeeeestern.dtlogs.slack;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SlackService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String botToken;

    public SlackService(@Value("${slack.bot-token:}") String botToken) {
        this.botToken = botToken;
    }

    public void postMessage(String channel, String text) {
        if (botToken == null || botToken.isEmpty()) {
            return;
        }
        String url = "https://slack.com/api/chat.postMessage";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(botToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = Map.of("channel", channel, "text", text);
        try {
            restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
        } catch (Exception ignored) {
        }
    }
}
