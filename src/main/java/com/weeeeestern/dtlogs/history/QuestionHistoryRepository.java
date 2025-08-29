package com.weeeeestern.dtlogs.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long> {
    List<QuestionHistory> findByUserIdAndCategory(String userId, String category);
    void deleteByUserIdAndCategory(String userId, String category);
}
