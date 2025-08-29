package com.weeeeestern.dtlogs.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TavilyClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey;
    private final String allowedSites;
    private final int daysLimit;

    public TavilyClient(
            @Value("${tavily.api-key:}") String apiKey,
            @Value("${tavily.allowed-sites:}") String allowedSites,
            @Value("${tavily.days-limit:1460}") int daysLimit) {
        this.apiKey = apiKey;
        this.allowedSites = allowedSites;
        this.daysLimit = daysLimit;
    }

    public String search(String query) {
        if (apiKey == null || apiKey.isEmpty()) {
            return "";
        }
        String url = "https://api.tavily.com/search";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("max_results", 1);
        body.put("include_domains", allowedSites.split(","));
        body.put("days", daysLimit);
        body.put("search_depth", "advanced");
        try {
            Map response = restTemplate.postForObject(url, new HttpEntity<>(body, headers), Map.class);
            if (response == null) return "";
            List<Map> results = (List<Map>) response.get("results");
            if (results == null || results.isEmpty()) return "";
            return (String) results.get(0).get("url");
        } catch (Exception e) {
            return "";
        }
    }
}
