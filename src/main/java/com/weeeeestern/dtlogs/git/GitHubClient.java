package com.weeeeestern.dtlogs.git;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class GitHubClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String token;
    private final String owner;
    private final String repo;
    private final String defaultBranch;

    public GitHubClient(
            @Value("${github.token:}") String token,
            @Value("${github.owner}") String owner,
            @Value("${github.repo}") String repo,
            @Value("${github.default-branch:develop}") String defaultBranch) {
        this.token = token;
        this.owner = owner;
        this.repo = repo;
        this.defaultBranch = defaultBranch;
    }

    public void commitAndCreatePR(String branch, String path, String content, String title, String body) {
        if (token == null || token.isEmpty()) return;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // get default branch sha
        Map ref = restTemplate.exchange(
                "https://api.github.com/repos/" + owner + "/" + repo + "/git/refs/heads/" + defaultBranch,
                org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(headers), Map.class).getBody();
        String baseSha = (String) ((Map) ref.get("object")).get("sha");

        // create branch
        Map<String, String> newRef = Map.of("ref", "refs/heads/" + branch, "sha", baseSha);
        restTemplate.postForEntity(
                "https://api.github.com/repos/" + owner + "/" + repo + "/git/refs",
                new HttpEntity<>(newRef, headers), Map.class);

        // commit file
        String encoded = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
        Map<String, Object> fileReq = Map.of(
                "message", title,
                "content", encoded,
                "branch", branch
        );
        restTemplate.exchange(
                "https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + path,
                org.springframework.http.HttpMethod.PUT,
                new HttpEntity<>(fileReq, headers), Map.class);

        // create PR
        Map<String, String> prReq = Map.of(
                "title", title,
                "head", branch,
                "base", defaultBranch,
                "body", body
        );
        restTemplate.postForEntity(
                "https://api.github.com/repos/" + owner + "/" + repo + "/pulls",
                new HttpEntity<>(prReq, headers), Map.class);

    }
}
