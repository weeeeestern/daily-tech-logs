package com.weeeeestern.dtlogs.md;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class MarkdownService {


    public String render(LocalDate date, String category, String question, String keywords, String link,
                          String userConcept, String userOral, String userExpressions, String userReflection) {
        return String.format("""
# 📘 오늘의 기술 블로그 정리
## 🗓 날짜 %s
## 📂 카테고리 %s
## ❓ 오늘의 질문
%s
## 🔑 추출 키워드
%s
## 🔗 추천 블로그
%s
## ✍️ 핵심 개념을 포함하여 정리
%s
## 💬 영어로 설명하듯이 정리
%s
## 🧾 오늘 배운 표현/기술 용어
%s
## ✏️ 느낀 점
%s
""", date, category, question, keywords, link, userConcept, userOral, userExpressions, userReflection);
    }

    public Path save(String content, LocalDate date, String category) throws IOException {
        Path dir = Path.of("daily-tech-logs");
        Files.createDirectories(dir);
        Path path = dir.resolve(String.format("%s-%s.md", date, category));
        Files.writeString(path, content, StandardCharsets.UTF_8);
        return path;

    }
}
