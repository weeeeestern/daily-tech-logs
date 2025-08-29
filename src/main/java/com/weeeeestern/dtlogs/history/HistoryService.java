package com.weeeeestern.dtlogs.history;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    private final QuestionHistoryRepository repository;
    private final Random random = new Random();

    public HistoryService(QuestionHistoryRepository repository) {
        this.repository = repository;
    }

    public String pickQuestion(String userId, String category, List<String> questions) {
        Set<String> asked = repository.findByUserIdAndCategory(userId, category).stream()
                .map(QuestionHistory::getQuestion)
                .collect(Collectors.toSet());
        List<String> remaining = questions.stream()
                .filter(q -> !asked.contains(q))
                .toList();
        if (remaining.isEmpty()) {
            return null;
        }
        return remaining.get(random.nextInt(remaining.size()));
    }

    public void recordPresented(String userId, String category, String question, String link) {
        repository.save(new QuestionHistory(userId, category, question, link,
                QuestionHistory.Status.PRESENTED, LocalDateTime.now()));
    }

    public void recordCompleted(String userId, String category, String question) {
        repository.save(new QuestionHistory(userId, category, question, null,
                QuestionHistory.Status.COMPLETED, LocalDateTime.now()));
    }

    public void resetCategory(String userId, String category) {
        repository.deleteByUserIdAndCategory(userId, category);
    }
}
