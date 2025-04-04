package com.ailab.assistant.dtos;

public record AIExplainRequest(
        String generatedContent,
        String selectedLine,
        String userQuestion
) {
}
