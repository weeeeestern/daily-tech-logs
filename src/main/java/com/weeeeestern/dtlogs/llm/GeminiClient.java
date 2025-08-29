package com.weeeeestern.dtlogs.llm;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeminiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey;
    private final String modelKeywords;
    private final String modelPrReview;

    public GeminiClient(
            @Value("${gemini.api-key:}") String apiKey,
            @Value("${gemini.model-keywords}") String modelKeywords,
            @Value("${gemini.model-pr-review}") String modelPrReview) {
        this.apiKey = apiKey;
        this.modelKeywords = modelKeywords;
        this.modelPrReview = modelPrReview;
    }

    public String extractKeywords(String text) {
        if (apiKey == null || apiKey.isEmpty()) return "";
        String prompt = "Extract 5-8 concise lowercase keywords separated by commas.\n" + text;
        return callModel(modelKeywords, prompt);
    }

    public String reviewPullRequest(String markdown) {
        if (apiKey == null || apiKey.isEmpty()) return "";
        String prompt = "Review the following markdown for English naturalness, technical accuracy, missing parts, " +
                "suggest better expressions/structure, and propose three deeper follow-up questions.\n" + markdown;
        return callModel(modelPrReview, prompt);
    }

    private String callModel(String model, String prompt) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
        Map<String, Object> req = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );
        try {
            ResponseEntity<Map> res = restTemplate.postForEntity(url, req, Map.class);
            List<Map> candidates = (List<Map>) res.getBody().get("candidates");
            if (candidates == null || candidates.isEmpty()) return "";
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "";
        }
    }
}
