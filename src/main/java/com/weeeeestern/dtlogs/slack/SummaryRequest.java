package com.weeeeestern.dtlogs.slack;

public record SummaryRequest(
        String userId,
        String category,
        String question,
        String link,
        String userConcept,
        String userOral,
        String userExpressions,
        String userReflection
) {}
