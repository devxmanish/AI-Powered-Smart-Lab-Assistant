package com.ailab.assistant.dtos;

public record LabManualGenerateRequest(
        String subjectName,
        String explanationLevel,
        String experimentName,
        String responseFormat
) {
}
