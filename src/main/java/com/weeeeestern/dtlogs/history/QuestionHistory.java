package com.weeeeestern.dtlogs.history;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question_history")
public class QuestionHistory {

    public enum Status { PRESENTED, COMPLETED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String category;
    @Column(length = 1000)
    private String question;
    private String link;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;

    public QuestionHistory() {}

    public QuestionHistory(String userId, String category, String question, String link, Status status, LocalDateTime createdAt) {
        this.userId = userId;
        this.category = category;
        this.question = question;
        this.link = link;
        this.status = status;
        this.createdAt = createdAt;
    }

    // getters and setters
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
