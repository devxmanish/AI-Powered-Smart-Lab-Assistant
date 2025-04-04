package com.ailab.assistant.dtos;

public record AIGenerateRequest(
        String category,
        String subject,
        String topic,
        String additionalKeywords,
        String explanationLevel // "detailed", "moderate", "concise"
) {

}
